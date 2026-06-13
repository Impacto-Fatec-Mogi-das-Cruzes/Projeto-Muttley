import { z } from "zod";
import type { Participant } from "@/models/participant/participant";

const intStringInRange = (requiredMsg: string, min: number, max: number) =>
  z
    .string()
    .trim()
    .min(1, requiredMsg)
    .regex(/^\d+$/, "Use apenas números inteiros positivos")
    .refine((value) => Number(value) >= min, `Deve ser no mínimo ${min}`)
    .refine((value) => Number(value) <= max, `Deve ser no máximo ${max}`);

const combineDateAndHour = (date: Date, hour: string) => {
  const [h, m] = hour.split(":");
  const result = new Date(date);
  result.setHours(Number(h) || 0, Number(m) || 0, 0, 0);
  return result;
};

export const editEventSchema = z.object({
  title: z.string().trim().min(1, "Informe o título").max(50, "Máximo de 50 caracteres"),
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
  workload: intStringInRange("Informe a carga horária", 1, 99),
  capacity: z
    .string()
    .trim()
    .optional()
    .refine(
      (value) => !value || (/^\d+$/.test(value) && Number(value) >= 1),
      "Use apenas números inteiros positivos",
    ),
  points: intStringInRange("Informe os pontos", 1, 100),
  modalityId: z.string().min(1, "Selecione a modalidade"),
  typeId: z.string().min(1, "Selecione o tipo do evento"),
  addressOrLink: z.string().trim().min(1, "Informe o local ou link"),
  subject: z.string().trim().min(1, "Informe o assunto").max(100, "Máximo de 100 caracteres"),
  competencias: z.array(z.string()),
  description: z.string().trim().max(255, "Máximo de 255 caracteres").optional(),
  organizers: z.array(z.custom<Participant>()).min(1, "Selecione ao menos um organizador"),
  speakers: z.array(z.custom<Participant>()),
  sponsors: z.array(z.custom<Participant>()),
  nameSignature: z.string().trim().min(1, "Informe o nome do responsável"),
  positionSignature: z.string().trim().min(1, "Informe o cargo / descrição"),
  signature: z.union([z.instanceof(File), z.null()]).optional(),
  background: z.union([z.instanceof(File), z.null()]).optional(),
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

export type EventFormValues = z.infer<typeof editEventSchema>;

export const onlyDigits = (value: string) => value.replace(/\D/g, "");
