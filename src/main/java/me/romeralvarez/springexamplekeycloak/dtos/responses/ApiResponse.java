package me.romeralvarez.springexamplekeycloak.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse {
  private String message;
  private int statusCode;
}
