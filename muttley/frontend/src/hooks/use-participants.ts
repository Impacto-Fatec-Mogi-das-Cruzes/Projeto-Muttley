import { keepPreviousData, useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  createParticipant,
  getParticipantEventHistory,
  listParticipants,
  type ListParticipantsParams,
} from "@/service/participantService/participant-service";

export function useParticipants(params: ListParticipantsParams, enabled = true) {
  return useQuery({
    queryKey: ["participants", params],
    queryFn: () => listParticipants(params),
    placeholderData: keepPreviousData,
    enabled,
  });
}

export function useParticipantHistory(
  participantId: string | undefined,
  params: { page: number; size: number },
) {
  return useQuery({
    queryKey: ["participant-history", participantId, params],
    queryFn: () => getParticipantEventHistory(participantId as string, params),
    enabled: !!participantId,
    placeholderData: keepPreviousData,
  });
}

export function useCreateParticipant() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createParticipant,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["participants"] });
    },
  });
}
