package com.example.intermediate.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

  @Id
  private Long userKey;

  @NotBlank
  private String userName;

  @NotBlank
  @Size(min = 4, max = 12)
  @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
  private String userId;

  @NotBlank
  @Size(min = 4, max = 32)
  @Pattern(regexp = "[a-z\\d]*${3,32}")
  private String userPassword;

  @NotBlank
  private String passwordConfirm;
}
