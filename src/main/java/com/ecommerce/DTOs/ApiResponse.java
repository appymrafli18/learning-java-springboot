package com.ecommerce.DTOs;


//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonPropertyOrder({"statusCode", "message", "data", "errors"})
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private int statusCode;
  private String message;
  private T data;
  private Map<String, String> errors;

  public ApiResponse() {
  }

  public ApiResponse(int statusCode, String message, T data) {
    this.statusCode = statusCode;
    this.message = message;
    this.data = data;
  }

  public ApiResponse(int statusCode, String message, T data, Map<String, String> errors) {
    this.statusCode = statusCode;
    this.message = message;
    this.data = data;
    this.errors = errors;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }
}
