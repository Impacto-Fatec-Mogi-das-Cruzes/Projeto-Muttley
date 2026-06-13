import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { toast } from "sonner";
import { CheckCircle2, Loader2 } from "lucide-react";
import type { UseMutationResult } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";

import { formatCpf, isValidCpf, unmaskCpf } from "@/lib/cpf";
import { getApiErrorMessage } from "@/lib/api-error";
import type { ParticipationRequest } from "@/models/public/participation-request";

const schema = z.object({
  name: z
    .string()
    .trim()
    .min(3, "Informe um nome válido.")
    .max(100, "Informe um nome válido."),
  email: z
    .string()
    .trim()
    .min(1, "Informe um e-mail válido.")
    .email("Informe um e-mail válido."),
  cpf: z.string().refine(isValidCpf, "CPF inválido."),
});

type FormValues = z.infer<typeof schema>;

interface PublicParticipationFormProps {
  title: string;
  successTitle: string;
  successDescription: string;
  mutation: UseMutationResult<void, unknown, ParticipationRequest>;
}

export function PublicParticipationForm({
  title,
  successTitle,
  successDescription,
  mutation,
}: PublicParticipationFormProps) {
  const form = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues: { name: "", email: "", cpf: "" },
  });

  const isPending = mutation.isPending;

  const onSubmit = form.handleSubmit(async (values) => {
    const payload: ParticipationRequest = {
      name: values.name.trim().replace(/\s+/g, " "),
      email: values.email.trim().toLowerCase(),
      cpf: unmaskCpf(values.cpf),
    };

    try {
      await mutation.mutateAsync(payload);
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível concluir sua solicitação."), {
        description: "Tente novamente em alguns instantes.",
      });
    }
  });

  return (
    <div className="flex min-h-screen items-center justify-center bg-background p-4">
      <div className="w-full max-w-md">
        <Card>
          {mutation.isSuccess ? (
            <CardContent className="flex flex-col items-center gap-4 py-10 text-center">
              <CheckCircle2 className="h-14 w-14 text-green-600" aria-hidden="true" />
              <div className="space-y-1.5">
                <h1 className="text-xl font-semibold tracking-tight">{successTitle}</h1>
                <p className="text-sm text-muted-foreground">{successDescription}</p>
              </div>
            </CardContent>
          ) : (
            <>
              <CardHeader className="text-center">
                <CardTitle>{title}</CardTitle>
                <CardDescription>Preencha seus dados abaixo para continuar.</CardDescription>
              </CardHeader>
              <CardContent>
                <Form {...form}>
                  <form onSubmit={onSubmit} noValidate>
                    <fieldset disabled={isPending} className="space-y-4">
                      <FormField
                        control={form.control}
                        name="name"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Nome</FormLabel>
                            <FormControl>
                              <Input autoFocus placeholder="Nome completo" {...field} />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="email"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>E-mail</FormLabel>
                            <FormControl>
                              <Input type="email" placeholder="email@exemplo.com" {...field} />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="cpf"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>CPF</FormLabel>
                            <FormControl>
                              <Input
                                placeholder="000.000.000-00"
                                inputMode="numeric"
                                name={field.name}
                                ref={field.ref}
                                value={field.value}
                                onBlur={field.onBlur}
                                onChange={(e) => field.onChange(formatCpf(e.target.value))}
                              />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <Button type="submit" className="w-full">
                        {isPending ? (
                          <>
                            <Loader2 className="animate-spin" aria-hidden="true" />
                            Enviando...
                          </>
                        ) : (
                          "Enviar"
                        )}
                      </Button>
                    </fieldset>
                  </form>
                </Form>
              </CardContent>
            </>
          )}
        </Card>
      </div>
    </div>
  );
}
