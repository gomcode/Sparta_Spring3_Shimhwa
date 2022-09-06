package com.sparta.spring3.controller.request;

import com.sparta.spring3.domain.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Clob;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
  @Id
  private Long postKey;

  private Timestamped createdAt;
  private Timestamped modifiedAt;
  private String title;
  private Long likeCount;
  private Long commentCount;
  private Clob imageUrl;
  private Clob content;
}
