package com.sparta.spring3.controller.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

  @NotBlank
  private String userId;

  @NotBlank
  private String userName;

  @NotBlank
  private String loginId;

  @NotBlank
  private String loginPw;

}
