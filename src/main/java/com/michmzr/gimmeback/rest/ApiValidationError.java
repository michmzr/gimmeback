package com.michmzr.gimmeback.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiValidationError {
  String object;
  String field;
  Object rejectedValue;
  String message;

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }

  @JsonCreator
  public ApiValidationError(
      @JsonProperty("object") String object,
      @JsonProperty("field") String field,
      @JsonProperty("rejectedValue") Object rejectedValue,
      @JsonProperty("message") String message) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }
}
