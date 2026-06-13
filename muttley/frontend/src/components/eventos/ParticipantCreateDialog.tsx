import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useCreateParticipant } from "@/hooks/use-participants";
import { formatCpf, isValidCpf, unmaskCpf } from "@/lib/cpf";
import { getApiErrorMessage } from "@/lib/api-error";
import type { Participant } from "@/models/participant/participant";

const schema = z.object({
  name: z.string().trim().min(1, "Informe o nome"),
  email: z.string().trim().min(1, "Informe o e-mail").email("E-mail inválido"),
  cpf: z.string().refine(isValidCpf, "CPF inválido"),
});

type FormValues = z.infer<typeof schema>;

interface ParticipantCreateDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onCreated: (participant: Participant) => void;
}

export function ParticipantCreateDialog({ open, onOpenChange, onCreated }: ParticipantCreateDialogProps) {
  const form = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues: { name: "", email: "", cpf: "" },
  });
  const createMutation = useCreateParticipant();

  const handleSubmit = form.handleSubmit(async (values) => {
    try {
      const participant = await createMutation.mutateAsync({
        name: values.name,
        email: values.email,
        cpf: unmaskCpf(values.cpf),
      });
      toast.success("Participante cadastrado com sucesso.");
      onCreated(participant);
      form.reset();
      onOpenChange(false);
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível cadastrar o participante."));
    }
  });

  return (
    <Dialog
      open={open}
      onOpenChange={(value) => {
        if (!value) form.reset();
        onOpenChange(value);
      }}
    >
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Novo Participante</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={handleSubmit} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome</FormLabel>
                  <FormControl>
                    <Input placeholder="Nome completo" {...field} />
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
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
                Cancelar
              </Button>
              <Button type="submit" disabled={createMutation.isPending}>
                {createMutation.isPending ? "Salvando..." : "Cadastrar"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
