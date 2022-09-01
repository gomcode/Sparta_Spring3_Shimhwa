package com.example.intermediate.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubcommentRequestDto {
  @Id
  private Long subcommentKey;

  private Long postKey;
  private Long userKey;

  @NotBlank
  private String subcomment;
}