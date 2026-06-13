import type { ReactNode } from "react";
import type { UseFormReturn } from "react-hook-form";
import { format, parseISO } from "date-fns";
import { ptBR } from "date-fns/locale";

import { Badge } from "@/components/ui/badge";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { Textarea } from "@/components/ui/textarea";
import {
  FormControl,
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

import { CompetenciasChipsField } from "@/components/eventos/CompetenciasChipsField";
import { DatePickerField } from "@/components/eventos/DatePickerField";
import { ImageReplaceField } from "@/components/eventos/ImageReplaceField";
import { ParticipantsEditor } from "@/components/eventos/ParticipantsEditor";
import { ViewField } from "@/components/eventos/ViewField";
import { onlyDigits, type EventFormValues } from "@/components/eventos/event-form-schema";

import { useEventModalities, useEventTypes } from "@/hooks/use-event-options";
import type { EventDetails, EventParticipantLink } from "@/models/event/event-details";

interface InformacoesTabProps {
  details: EventDetails;
  mode: "view" | "edit";
  form: UseFormReturn<EventFormValues>;
}

const formatPtDate = (iso?: string | null) =>
  iso ? format(parseISO(iso), "dd/MM/yyyy", { locale: ptBR }) : null;

const formatPtDateTime = (iso?: string | null, hour?: string | null) => {
  const date = formatPtDate(iso);
  if (!date) return null;
  return hour ? `${date} ${hour}` : date;
};

const formatDateTimeRange = (details: EventDetails) => {
  const start = formatPtDateTime(details.startDate, details.startHour);
  if (!start) return null;

  const multiDay =
    formatPtDate(details.endDate) && formatPtDate(details.endDate) !== formatPtDate(details.startDate);
  if (multiDay) {
    return `${start} - ${formatPtDateTime(details.endDate, details.endHour)}`;
  }
  return details.endHour ? `${start} - ${details.endHour}` : start;
};

const isUrl = (value: string) => /^https?:\/\//i.test(value.trim());

export function InformacoesTab({ details, mode, form }: InformacoesTabProps) {
  const editing = mode === "edit";

  return (
    <Card className="p-6 space-y-8">
      <Section title="Divulgação">
        <div className="grid gap-4 sm:grid-cols-2">
          {editing ? (
            <TipoSelect form={form} />
          ) : (
            <ViewField label="Tipo do Evento" value={details.typeName} />
          )}

          {editing ? (
            <FormField
              control={form.control}
              name="subject"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Assunto do Evento</FormLabel>
                  <FormControl>
                    <Input maxLength={100} placeholder="Assunto principal" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          ) : (
            <ViewField label="Assunto do Evento" value={details.subject} />
          )}
        </div>

        {editing ? (
          <FormField
            control={form.control}
            name="competencias"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Competências</FormLabel>
                <CompetenciasChipsField value={field.value} onChange={field.onChange} />
                <FormMessage />
              </FormItem>
            )}
          />
        ) : (
          <ViewField label="Competências" value={<KeywordsBadges keywords={details.keywords} />} />
        )}

        {editing ? (
          <FormField
            control={form.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Descrição</FormLabel>
                <FormControl>
                  <Textarea
                    rows={3}
                    maxLength={255}
                    placeholder="Descrição do evento"
                    {...field}
                    value={field.value ?? ""}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        ) : (
          <ViewField
            label="Descrição"
            value={
              details.description ? (
                <span className="whitespace-pre-line">{details.description}</span>
              ) : null
            }
          />
        )}
      </Section>

      <Section title="Informações Básicas">
        <div className="grid gap-4 sm:grid-cols-2">
          {editing ? (
            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Título</FormLabel>
                  <FormControl>
                    <Input maxLength={50} placeholder="Nome do evento" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          ) : (
            <ViewField label="Título" value={details.title} />
          )}

          {editing ? (
            <div className="grid grid-cols-2 gap-3">
              <FormField
                control={form.control}
                name="startDate"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Data Inicial</FormLabel>
                    <DatePickerField value={field.value} onChange={field.onChange} />
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="startHour"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Hora Inicial</FormLabel>
                    <FormControl>
                      <Input type="time" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="endDate"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Data Final</FormLabel>
                    <DatePickerField value={field.value} onChange={field.onChange} />
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="endHour"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Hora Final</FormLabel>
                    <FormControl>
                      <Input type="time" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
          ) : (
            <ViewField
              label="Datas e Horários"
              value={formatDateTimeRange(details)}
            />
          )}

          {editing ? (
            <NumericField
              form={form}
              name="workload"
              label="Carga Horária"
              suffix="horas"
              placeholder="0"
            />
          ) : (
            <ViewField label="Carga Horária" value={`${details.workload} horas`} />
          )}

          {editing ? (
            <NumericField form={form} name="points" label="Pontos" suffix="pontos" placeholder="0" />
          ) : (
            <ViewField label="Pontos" value={details.points} />
          )}
        </div>
      </Section>

      <Section title="Local">
        <div className="grid gap-4 sm:grid-cols-2">
          {editing ? (
            <ModalidadeSelect form={form} />
          ) : (
            <ViewField label="Modalidade" value={details.modalityName} />
          )}

          {editing ? (
            <NumericField form={form} name="capacity" label="Capacidade" placeholder="Opcional" />
          ) : (
            <ViewField label="Capacidade" value={details.capacity ?? null} />
          )}

          {editing ? (
            <FormField
              control={form.control}
              name="addressOrLink"
              render={({ field }) => (
                <FormItem className="sm:col-span-2">
                  <FormLabel>Endereço / Link</FormLabel>
                  <FormControl>
                    <Input placeholder="Endereço físico ou URL" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          ) : (
            <ViewField
              className="sm:col-span-2"
              label="Endereço / Link"
              value={
                details.addressOrLink ? (
                  isUrl(details.addressOrLink) ? (
                    <a
                      href={details.addressOrLink}
                      target="_blank"
                      rel="noreferrer"
                      className="text-primary hover:underline break-all"
                    >
                      {details.addressOrLink}
                    </a>
                  ) : (
                    details.addressOrLink
                  )
                ) : null
              }
            />
          )}
        </div>
      </Section>

      <Section title="Organização">
        {editing ? (
          <FormField
            control={form.control}
            name="organizers"
            render={() => (
              <FormItem>
                <ParticipantsEditor
                  organizers={form.watch("organizers")}
                  speakers={form.watch("speakers")}
                  sponsors={form.watch("sponsors")}
                  onOrganizersChange={(v) =>
                    form.setValue("organizers", v, { shouldValidate: true, shouldDirty: true })
                  }
                  onSpeakersChange={(v) =>
                    form.setValue("speakers", v, { shouldDirty: true })
                  }
                  onSponsorsChange={(v) =>
                    form.setValue("sponsors", v, { shouldDirty: true })
                  }
                />
                <FormMessage />
              </FormItem>
            )}
          />
        ) : (
          <div className="grid gap-4 sm:grid-cols-3">
            <ParticipantsColumn title="Organizadores" people={details.organizers} />
            <ParticipantsColumn title="Palestrantes" people={details.speakers} />
            <ParticipantsColumn title="Patrocinadores" people={details.sponsors} />
          </div>
        )}
      </Section>

      <Section title="Certificado">
        <div className="grid gap-4 sm:grid-cols-2">
          <div className="sm:col-span-2">
            <p className="mb-2 text-xs text-muted-foreground">Background do Certificado</p>
            {editing ? (
              <FormField
                control={form.control}
                name="background"
                render={({ field }) => (
                  <FormItem>
                    <ImageReplaceField
                      currentUrl={details.imageBackgroundUrl}
                      value={field.value ?? null}
                      onChange={field.onChange}
                      alt="Background do certificado"
                      previewClassName="max-h-48"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />
            ) : details.imageBackgroundUrl ? (
              <div className="overflow-hidden rounded-md border border-border bg-muted/30">
                <img
                  src={details.imageBackgroundUrl}
                  alt="Background do certificado"
                  className="max-h-48 w-full object-contain p-2"
                />
              </div>
            ) : (
              <span className="text-sm">-</span>
            )}
          </div>

          <div className="sm:col-span-2">
            <p className="mb-2 text-xs text-muted-foreground">Assinatura do certificado</p>
            {editing ? (
              <FormField
                control={form.control}
                name="signature"
                render={({ field }) => (
                  <FormItem>
                    <ImageReplaceField
                      currentUrl={details.signatureImageUrl}
                      value={field.value ?? null}
                      onChange={field.onChange}
                      alt="Assinatura"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />
            ) : details.signatureImageUrl ? (
              <div className="overflow-hidden rounded-md border border-border bg-muted/30">
                <img
                  src={details.signatureImageUrl}
                  alt="Assinatura"
                  className="max-h-24 w-full object-contain p-2"
                />
              </div>
            ) : (
              <span className="text-sm">-</span>
            )}
          </div>

          {editing ? (
            <FormField
              control={form.control}
              name="nameSignature"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome do Signatário</FormLabel>
                  <FormControl>
                    <Input placeholder="Nome de quem assina" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          ) : (
            <ViewField label="Nome do Signatário" value={details.nameSignature} />
          )}

          {editing ? (
            <FormField
              control={form.control}
              name="positionSignature"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cargo do Signatário</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Diretor da Fatec ZL" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          ) : (
            <ViewField label="Cargo do Signatário" value={details.positionSignature} />
          )}
        </div>
      </Section>
    </Card>
  );
}

function Section({ title, children }: { title: string; children: ReactNode }) {
  return (
    <section className="space-y-4">
      <div>
        <h4 className="text-sm font-semibold">{title}</h4>
        <Separator className="mt-2" />
      </div>
      {children}
    </section>
  );
}

function KeywordsBadges({ keywords }: { keywords?: string | null }) {
  const items = (keywords ?? "")
    .split(",")
    .map((k) => k.trim())
    .filter(Boolean);
  if (items.length === 0) return null;
  return (
    <div className="flex flex-wrap gap-1.5">
      {items.map((item) => (
        <Badge key={item} variant="secondary">
          {item}
        </Badge>
      ))}
    </div>
  );
}

function ParticipantsColumn({ title, people }: { title: string; people: EventParticipantLink[] }) {
  return (
    <div className="space-y-1">
      <p className="text-xs text-muted-foreground">{title}</p>
      {people.length === 0 ? (
        <p className="text-sm">-</p>
      ) : (
        <ul className="space-y-0.5">
          {people.map((person) => (
            <li key={person.participantId} className="text-sm" title={person.email ?? undefined}>
              {person.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

function TipoSelect({ form }: { form: UseFormReturn<EventFormValues> }) {
  const { data: types, isLoading, isError } = useEventTypes();
  return (
    <FormField
      control={form.control}
      name="typeId"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Tipo do Evento</FormLabel>
          <Select value={field.value} onValueChange={field.onChange} disabled={isLoading}>
            <FormControl>
              <SelectTrigger>
                <SelectValue
                  placeholder={isLoading ? "Carregando..." : isError ? "Erro ao carregar" : "Selecione o tipo"}
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
  );
}

function ModalidadeSelect({ form }: { form: UseFormReturn<EventFormValues> }) {
  const { data: modalities, isLoading, isError } = useEventModalities();
  return (
    <FormField
      control={form.control}
      name="modalityId"
      render={({ field }) => (
        <FormItem>
          <FormLabel>Modalidade</FormLabel>
          <Select value={field.value} onValueChange={field.onChange} disabled={isLoading}>
            <FormControl>
              <SelectTrigger>
                <SelectValue
                  placeholder={
                    isLoading ? "Carregando..." : isError ? "Erro ao carregar" : "Selecione a modalidade"
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
  );
}

function NumericField({
  form,
  name,
  label,
  suffix,
  placeholder,
}: {
  form: UseFormReturn<EventFormValues>;
  name: "workload" | "points" | "capacity";
  label: string;
  suffix?: string;
  placeholder?: string;
}) {
  return (
    <FormField
      control={form.control}
      name={name}
      render={({ field }) => (
        <FormItem>
          <FormLabel>{label}</FormLabel>
          <div className="flex items-center gap-2">
            <FormControl>
              <Input
                inputMode="numeric"
                placeholder={placeholder}
                value={field.value ?? ""}
                onChange={(e) => field.onChange(onlyDigits(e.target.value))}
                onBlur={field.onBlur}
                name={field.name}
                ref={field.ref}
              />
            </FormControl>
            {suffix && <span className="text-sm text-muted-foreground">{suffix}</span>}
          </div>
          <FormMessage />
        </FormItem>
      )}
    />
  );
}
