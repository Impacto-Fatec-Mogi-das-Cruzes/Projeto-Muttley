import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { format, parseISO } from "date-fns";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Form } from "@/components/ui/form";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

import { ConfirmDialog } from "@/components/eventos/ConfirmDialog";
import { EventDetailHeader } from "@/components/eventos/EventDetailHeader";
import { InformacoesTab } from "@/components/eventos/InformacoesTab";
import { LinksTab } from "@/components/eventos/LinksTab";
import { ParticipantsTab } from "@/components/eventos/ParticipantsTab";
import {
  editEventSchema,
  type EventFormValues,
} from "@/components/eventos/event-form-schema";

import { useEventDetails } from "@/hooks/use-event-details";
import { useUpdateEvent } from "@/hooks/use-update-event";
import { useDeleteEvent } from "@/hooks/use-delete-event";
import { useFinalizeEvent } from "@/hooks/use-finalize-event";
import { getApiErrorMessage } from "@/lib/api-error";
import { isEventFinished } from "@/lib/event-status";
import type { EventDetails, EventParticipantLink } from "@/models/event/event-details";
import type { EventUpdateRequest } from "@/models/event/event-update-request";
import type { Participant } from "@/models/participant/participant";

const toParticipant = (link: EventParticipantLink): Participant => ({
  id: link.participantId,
  name: link.name,
  cpf: link.cpf,
  email: link.email ?? undefined,
});

const buildFormValues = (details: EventDetails): EventFormValues => ({
  title: details.title,
  startDate: parseISO(details.startDate),
  endDate: details.endDate ? parseISO(details.endDate) : undefined,
  startHour: details.startHour ?? "",
  endHour: details.endHour ?? "",
  workload: String(details.workload),
  capacity: details.capacity != null ? String(details.capacity) : "",
  points: String(details.points),
  modalityId: details.modalityId,
  typeId: details.typeId,
  addressOrLink: details.addressOrLink,
  subject: details.subject,
  competencias: (details.keywords ?? "")
    .split(",")
    .map((k) => k.trim())
    .filter(Boolean),
  description: details.description ?? "",
  organizers: details.organizers.map(toParticipant),
  speakers: details.speakers.map(toParticipant),
  sponsors: details.sponsors.map(toParticipant),
  nameSignature: details.nameSignature,
  positionSignature: details.positionSignature,
  signature: null,
  background: null,
});

export default function EventoDetalhes() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { data: details, isLoading, isError, refetch } = useEventDetails(id);

  const [mode, setMode] = useState<"view" | "edit">("view");
  const [tab, setTab] = useState("info");
  const [saveOpen, setSaveOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [finalizeOpen, setFinalizeOpen] = useState(false);

  const form = useForm<EventFormValues>({
    resolver: zodResolver(editEventSchema),
    defaultValues: {
      title: "",
      startDate: undefined,
      endDate: undefined,
      startHour: "",
      endHour: "",
      workload: "",
      capacity: "",
      points: "",
      modalityId: "",
      typeId: "",
      addressOrLink: "",
      subject: "",
      competencias: [],
      description: "",
      organizers: [],
      speakers: [],
      sponsors: [],
      nameSignature: "",
      positionSignature: "",
      signature: null,
      background: null,
    },
  });

  const updateEvent = useUpdateEvent();
  const deleteEvent = useDeleteEvent();
  const finalizeEvent = useFinalizeEvent();

  const enterEdit = () => {
    if (!details) return;
    form.reset(buildFormValues(details));
    setTab("info");
    setMode("edit");
  };

  const cancelEdit = () => {
    setMode("view");
  };

  const handleSaveClick = async () => {
    const valid = await form.trigger();
    if (valid) {
      setSaveOpen(true);
    } else {
      toast.error("Verifique os campos destacados antes de salvar.");
    }
  };

  const onValidSave = async (values: EventFormValues) => {
    if (!details) return;
    const keywords = values.competencias.map((c) => c.trim()).filter(Boolean).join(",");

    const event: EventUpdateRequest = {
      id: details.id,
      title: values.title.trim(),
      startDate: format(values.startDate, "yyyy-MM-dd"),
      endDate: format(values.endDate, "yyyy-MM-dd"),
      startHour: values.startHour,
      endHour: values.endHour,
      workload: Number(values.workload),
      ...(values.capacity ? { capacity: Number(values.capacity) } : {}),
      points: Number(values.points),
      typeId: values.typeId,
      modalityId: values.modalityId,
      addressOrLink: values.addressOrLink.trim(),
      subject: values.subject.trim(),
      ...(keywords ? { keywords } : {}),
      ...(values.description?.trim() ? { description: values.description.trim() } : {}),
      organizerIds: values.organizers.map((p) => p.id),
      speakerIds: values.speakers.map((p) => p.id),
      sponsorIds: values.sponsors.map((p) => p.id),
      nameSignature: values.nameSignature.trim(),
      positionSignature: values.positionSignature.trim(),
    };

    try {
      await updateEvent.mutateAsync({
        event,
        files: {
          signature: values.signature ?? undefined,
          background: values.background ?? undefined,
        },
      });
      toast.success("Evento atualizado com sucesso!");
      setSaveOpen(false);
      setMode("view");
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível salvar as alterações."));
    }
  };

  const handleDelete = async () => {
    if (!id) return;
    try {
      await deleteEvent.mutateAsync(id);
      toast.success("Evento excluído com sucesso.");
      setDeleteOpen(false);
      navigate("/eventos");
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível excluir o evento."));
    }
  };

  const handleFinalize = async () => {
    if (!id) return;
    try {
      await finalizeEvent.mutateAsync(id);
      toast.success("Evento finalizado com sucesso.");
      setFinalizeOpen(false);
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível finalizar o evento."));
    }
  };

  if (isLoading) {
    return (
      <div className="mx-auto max-w-5xl space-y-6 animate-fade-in">
        <div className="flex items-center justify-between">
          <Skeleton className="h-8 w-64" />
          <Skeleton className="h-9 w-48" />
        </div>
        <Skeleton className="h-10 w-80" />
        <Card className="space-y-4 p-6">
          <Skeleton className="h-5 w-40" />
          <Skeleton className="h-4 w-full" />
          <Skeleton className="h-4 w-3/4" />
        </Card>
      </div>
    );
  }

  if (isError || !details) {
    return (
      <div className="mx-auto flex max-w-5xl flex-col items-center justify-center gap-4 py-20 text-center animate-fade-in">
        <p className="text-muted-foreground">
          {isError
            ? "Não foi possível carregar os detalhes do evento."
            : "Evento não encontrado."}
        </p>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => navigate("/eventos")}>
            Voltar
          </Button>
          {isError && <Button onClick={() => refetch()}>Tentar novamente</Button>}
        </div>
      </div>
    );
  }

  const isFinished = isEventFinished(details.statusName);

  return (
    <div className="mx-auto max-w-5xl space-y-6 animate-fade-in">
      <Form {...form}>
        <EventDetailHeader
          title={details.title}
          statusName={details.statusName}
          mode={mode}
          isFinished={isFinished}
          saving={updateEvent.isPending}
          onEdit={enterEdit}
          onDelete={() => setDeleteOpen(true)}
          onFinalize={() => setFinalizeOpen(true)}
          onCancel={cancelEdit}
          onSave={handleSaveClick}
        />

        <Tabs value={tab} onValueChange={setTab}>
          <TabsList>
            <TabsTrigger value="info">Informações</TabsTrigger>
            <TabsTrigger value="participants" disabled={mode === "edit"}>
              Participantes
            </TabsTrigger>
            <TabsTrigger value="links" disabled={mode === "edit"}>
              Links
            </TabsTrigger>
          </TabsList>

          <TabsContent value="info">
            <InformacoesTab details={details} mode={mode} form={form} />
          </TabsContent>
          <TabsContent value="participants">
            {id && <ParticipantsTab eventId={id} />}
          </TabsContent>
          <TabsContent value="links">
            <LinksTab details={details} />
          </TabsContent>
        </Tabs>
      </Form>

      <ConfirmDialog
        open={saveOpen}
        onOpenChange={setSaveOpen}
        title="Salvar alterações?"
        description="As informações do evento serão atualizadas."
        confirmLabel="Salvar Alterações"
        loading={updateEvent.isPending}
        onConfirm={() => form.handleSubmit(onValidSave)()}
      />

      <ConfirmDialog
        open={deleteOpen}
        onOpenChange={setDeleteOpen}
        title="Excluir evento?"
        description="Esta ação é irreversível e removerá o evento permanentemente."
        confirmLabel="Excluir"
        destructive
        loading={deleteEvent.isPending}
        onConfirm={handleDelete}
      />

      <ConfirmDialog
        open={finalizeOpen}
        onOpenChange={setFinalizeOpen}
        title="Finalizar evento?"
        description="O evento será marcado como finalizado."
        confirmLabel="Finalizar"
        loading={finalizeEvent.isPending}
        onConfirm={handleFinalize}
      />
    </div>
  );
}
