package com.example.intermediate.controller.request;

import com.example.intermediate.domain.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;

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
