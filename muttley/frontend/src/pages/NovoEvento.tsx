import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { format } from "date-fns";
import { Link, useNavigate } from "react-router-dom";
import { ArrowLeft, CalendarPlus } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

import { DatePickerField } from "@/components/eventos/DatePickerField";
import { ImageUploadField } from "@/components/eventos/ImageUploadField";
import { ResponsibleSection } from "@/components/eventos/ResponsibleSection";

import { useEventModalities, useEventTypes } from "@/hooks/use-event-options";
import { useCreateEvent } from "@/hooks/use-create-event";
import { getApiErrorMessage } from "@/lib/api-error";
import type { EventRequest } from "@/models/event/event-request";
import type { Participant } from "@/models/participant/participant";

const positiveIntString = (requiredMsg: string) =>
  z
    .string()
    .trim()
    .min(1, requiredMsg)
    .regex(/^\d+$/, "Use apenas números inteiros positivos")
    .refine((value) => Number(value) >= 1, "Deve ser maior que zero");

const imageFile = (requiredMsg: string) =>
  z
    .union([z.instanceof(File), z.null()])
    .refine((file) => file instanceof File, requiredMsg)
    .refine((file) => !(file instanceof File) || file.type.startsWith("image/"), "Formato de imagem inválido");

const combineDateAndHour = (date: Date, hour: string) => {
  const [h, m] = hour.split(":");
  const result = new Date(date);
  result.setHours(Number(h) || 0, Number(m) || 0, 0, 0);
  return result;
};

const schema = z.object({
  title: z.string().trim().min(1, "Informe o título").max(30, "Máximo de 30 caracteres"),
  startDate: z.date({
    required_error: "Selecione a data inicial",
    invalid_type_error: "Selecione a data inicial",
  }),
  endDate: z.date({
    required_error: "Selecione a data final",
    invalid_type_error: "Selecione a data final",
  }),
  startHour: z.string().min(1, "Informe a hora inicial"),
  endHour: z.string().min(1, "Informe a hora final"),
  workload: positiveIntString("Informe a carga horária"),
  capacity: z
    .string()
    .trim()
    .optional()
    .refine((value) => !value || (/^\d+$/.test(value) && Number(value) >= 1), "Use apenas números inteiros positivos"),
  points: positiveIntString("Informe os pontos"),
  modalityId: z.string().min(1, "Selecione a modalidade"),
  typeId: z.string().min(1, "Selecione o tipo do evento"),
  addressOrLink: z.string().trim().min(1, "Informe o local ou link"),
  subject: z.string().trim().min(1, "Informe o assunto").max(100, "Máximo de 100 caracteres"),
  keywords: z.string().trim().max(180, "Máximo de 180 caracteres").optional(),
  description: z.string().trim().max(255, "Máximo de 255 caracteres").optional(),
  organizers: z.array(z.custom<Participant>()).min(1, "Selecione ao menos um organizador"),
  speakers: z.array(z.custom<Participant>()),
  sponsors: z.array(z.custom<Participant>()),
  nameSignature: z.string().trim().min(1, "Informe o nome do responsável"),
  positionSignature: z.string().trim().min(1, "Informe o cargo / descrição"),
  background: imageFile("Envie o background do certificado"),
  signature: imageFile("Envie a assinatura"),
}).refine(
  (data) => {
    if (!(data.startDate instanceof Date) || !(data.endDate instanceof Date)) return true;
    if (!data.startHour || !data.endHour) return true;
    return (
      combineDateAndHour(data.endDate, data.endHour) >=
      combineDateAndHour(data.startDate, data.startHour)
    );
  },
  { message: "A data/hora final deve ser igual ou posterior à inicial", path: ["endDate"] },
);

type FormValues = z.infer<typeof schema>;

const onlyDigits = (value: string) => value.replace(/\D/g, "");

export default function NovoEvento() {
  const navigate = useNavigate();
  const createEvent = useCreateEvent();

  const { data: modalities, isLoading: loadingModalities, isError: modalitiesError } = useEventModalities();
  const { data: types, isLoading: loadingTypes, isError: typesError } = useEventTypes();

  const form = useForm<FormValues>({
    resolver: zodResolver(schema),
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
      keywords: "",
      description: "",
      organizers: [],
      speakers: [],
      sponsors: [],
      nameSignature: "",
      positionSignature: "",
      background: null,
      signature: null,
    },
  });

  const organizers = form.watch("organizers");
  const speakers = form.watch("speakers");
  const sponsors = form.watch("sponsors");
  const idsOf = (list: Participant[]) => list.map((participant) => participant.id);

  const onSubmit = form.handleSubmit(async (values) => {
    const keywords = (values.keywords ?? "")
      .split(";")
      .map((keyword) => keyword.trim())
      .filter(Boolean)
      .join(",");

    const event: EventRequest = {
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
      organizerIds: values.organizers.map((participant) => participant.id),
      speakerIds: values.speakers.map((participant) => participant.id),
      sponsorIds: values.sponsors.map((participant) => participant.id),
      nameSignature: values.nameSignature.trim(),
      positionSignature: values.positionSignature.trim(),
    };

    try {
      await createEvent.mutateAsync({
        event,
        files: { background: values.background as File, signature: values.signature as File },
      });
      toast.success("Evento criado com sucesso!");
      navigate("/eventos");
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível criar o evento."));
    }
  });

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <div className="flex items-center gap-3">
        <Button asChild variant="ghost" size="icon">
          <Link to="/eventos" aria-label="Voltar">
            <ArrowLeft className="h-5 w-5" />
          </Link>
        </Button>
        <CalendarPlus className="h-6 w-6 text-primary" />
        <h1 className="text-2xl font-bold tracking-tight">Novo Evento</h1>
      </div>

      <Form {...form}>
        <form onSubmit={onSubmit} className="space-y-8">
          <section className="space-y-4 rounded-xl border border-border bg-card p-5 shadow-sm">
            <h3 className="text-sm font-semibold text-primary">Informações Gerais</h3>

            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Título *</FormLabel>
                  <FormControl>
                    <Input maxLength={30} placeholder="Nome do evento" {...field} />
                  </FormControl>
                  <FormDescription>{field.value?.length ?? 0}/30</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid gap-4 sm:grid-cols-2">
              <div className="grid grid-cols-2 gap-3">
                <FormField
                  control={form.control}
                  name="startDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Data Inicial *</FormLabel>
                      <DatePickerField value={field.value} onChange={field.onChange} placeholder="Selecione a data" />
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="startHour"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Hora Inicial *</FormLabel>
                      <FormControl>
                        <Input type="time" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <FormField
                  control={form.control}
                  name="endDate"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Data Final *</FormLabel>
                      <DatePickerField value={field.value} onChange={field.onChange} placeholder="Selecione a data" />
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="endHour"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Hora Final *</FormLabel>
                      <FormControl>
                        <Input type="time" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
            </div>

            <div className="grid gap-4 sm:grid-cols-3">
              <FormField
                control={form.control}
                name="workload"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Carga Horária *</FormLabel>
                    <div className="flex items-center gap-2">
                      <FormControl>
                        <Input
                          inputMode="numeric"
                          placeholder="0"
                          value={field.value}
                          onChange={(e) => field.onChange(onlyDigits(e.target.value))}
                          onBlur={field.onBlur}
                          name={field.name}
                          ref={field.ref}
                        />
                      </FormControl>
                      <span className="text-sm text-muted-foreground">horas</span>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="points"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Pontos *</FormLabel>
                    <div className="flex items-center gap-2">
                      <FormControl>
                        <Input
                          inputMode="numeric"
                          placeholder="0"
                          value={field.value}
                          onChange={(e) => field.onChange(onlyDigits(e.target.value))}
                          onBlur={field.onBlur}
                          name={field.name}
                          ref={field.ref}
                        />
                      </FormControl>
                      <span className="text-sm text-muted-foreground">pontos</span>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="capacity"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Capacidade</FormLabel>
                    <FormControl>
                      <Input
                        inputMode="numeric"
                        placeholder="Opcional"
                        value={field.value ?? ""}
                        onChange={(e) => field.onChange(onlyDigits(e.target.value))}
                        onBlur={field.onBlur}
                        name={field.name}
                        ref={field.ref}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="modalityId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Modalidade *</FormLabel>
                    <Select value={field.value} onValueChange={field.onChange} disabled={loadingModalities}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue
                            placeholder={
                              loadingModalities
                                ? "Carregando..."
                                : modalitiesError
                                  ? "Erro ao carregar"
                                  : "Selecione a modalidade"
                            }
                          />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {modalities?.map((modality) => (
                          <SelectItem key={modality.id} value={modality.id}>
                            {modality.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="typeId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Tipo do Evento *</FormLabel>
                    <Select value={field.value} onValueChange={field.onChange} disabled={loadingTypes}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue
                            placeholder={
                              loadingTypes ? "Carregando..." : typesError ? "Erro ao carregar" : "Selecione o tipo"
                            }
                          />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {types?.map((type) => (
                          <SelectItem key={type.id} value={type.id}>
                            {type.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="addressOrLink"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Local / Link *</FormLabel>
                  <FormControl>
                    <Input placeholder="Endereço físico ou URL" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </section>

          <section className="space-y-4 rounded-xl border border-border bg-card p-5 shadow-sm">
            <h3 className="text-sm font-semibold text-primary">Conteúdo</h3>

            <FormField
              control={form.control}
              name="subject"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Assunto do Evento *</FormLabel>
                  <FormControl>
                    <Input maxLength={100} placeholder="Assunto principal" {...field} />
                  </FormControl>
                  <FormDescription>{field.value?.length ?? 0}/100</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="keywords"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Palavras-chave</FormLabel>
                  <FormControl>
                    <Input maxLength={180} placeholder="React; Tailwind; IA" {...field} value={field.value ?? ""} />
                  </FormControl>
                  <FormDescription>
                    Separe por ponto e vírgula (;). {field.value?.length ?? 0}/180
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Descrição</FormLabel>
                  <FormControl>
                    <Textarea
                      maxLength={255}
                      rows={3}
                      placeholder="Descrição do evento (opcional)"
                      {...field}
                      value={field.value ?? ""}
                    />
                  </FormControl>
                  <FormDescription>{field.value?.length ?? 0}/255</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </section>

          <section className="space-y-4 rounded-xl border border-border bg-card p-5 shadow-sm">
            <h3 className="text-sm font-semibold text-primary">Responsáveis</h3>

            <FormField
              control={form.control}
              name="organizers"
              render={({ field }) => (
                <FormItem>
                  <ResponsibleSection
                    label="Organizadores"
                    pickerTitle="Selecionar Organizadores"
                    required
                    selected={field.value}
                    onChange={field.onChange}
                    excludeIds={[...idsOf(speakers), ...idsOf(sponsors)]}
                  />
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="speakers"
              render={({ field }) => (
                <FormItem>
                  <ResponsibleSection
                    label="Palestrantes"
                    pickerTitle="Selecionar Palestrantes"
                    selected={field.value}
                    onChange={field.onChange}
                    excludeIds={[...idsOf(organizers), ...idsOf(sponsors)]}
                  />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="sponsors"
              render={({ field }) => (
                <FormItem>
                  <ResponsibleSection
                    label="Patrocinadores"
                    pickerTitle="Selecionar Patrocinadores"
                    selected={field.value}
                    onChange={field.onChange}
                    excludeIds={[...idsOf(organizers), ...idsOf(speakers)]}
                  />
                </FormItem>
              )}
            />
          </section>

          <section className="space-y-4 rounded-xl border border-border bg-card p-5 shadow-sm">
            <h3 className="text-sm font-semibold text-primary">Template do Certificado</h3>

            <div className="grid gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="background"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Background do Certificado *</FormLabel>
                    <ImageUploadField value={field.value ?? null} onChange={field.onChange} />
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="signature"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Assinatura *</FormLabel>
                    <ImageUploadField value={field.value ?? null} onChange={field.onChange} />
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="nameSignature"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nome do Responsável *</FormLabel>
                    <FormControl>
                      <Input placeholder="Nome de quem assina" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="positionSignature"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Cargo / Descrição *</FormLabel>
                    <FormControl>
                      <Input placeholder="Ex: Diretor da Fatec ZL" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
          </section>

          <div className="flex justify-end gap-3">
            <Button asChild variant="outline" type="button">
              <Link to="/eventos">Cancelar</Link>
            </Button>
            <Button type="submit" disabled={createEvent.isPending}>
              {createEvent.isPending ? "Criando..." : "Criar Evento"}
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
