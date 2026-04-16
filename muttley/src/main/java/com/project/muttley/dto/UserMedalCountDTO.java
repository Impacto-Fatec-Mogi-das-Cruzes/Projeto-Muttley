package com.project.muttley.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMedalCountDTO {
   private Long userId;
   private String username;
   private Long medalCount;

   public UserMedalCountDTO(Long userId, String username, Long medalCount) {
      this.userId = userId;
      this.username = username;
      this.medalCount = medalCount;
   }
}
