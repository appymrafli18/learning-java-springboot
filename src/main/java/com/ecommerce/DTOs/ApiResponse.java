package com.ecommerce.DTOs;


//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonPropertyOrder({"statusCode", "message", "data", "errors"})
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data // Membuat getter, setter, equals, canEqual, hashCode, dan toString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
  private int statusCode;
  private String message;
  private T data;
  private Map<String, String> errors;
}
