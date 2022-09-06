package com.sparta.spring3.controller.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
  private Long userId;
  private String userName;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
