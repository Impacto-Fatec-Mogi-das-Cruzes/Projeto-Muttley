import { useMutation, useQueryClient } from "@tanstack/react-query";
import { rewardParticipants } from "@/service/eventService/event-service";
import type { RewardRequest } from "@/models/event/reward-request";

interface RewardVariables {
  eventId: string;
  payload: RewardRequest;
}

export function useRewardParticipants() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ eventId, payload }: RewardVariables) => rewardParticipants(eventId, payload),
    onSuccess: (_data, { eventId }) => {
      queryClient.invalidateQueries({ queryKey: ["event-details", eventId] });
    },
  });
}
