package com.michmzr.gimmeback.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions

public class ErrorResponseAssert {
    private ObjectMapper objectMapper
    private ApiError apiError

    public ErrorResponseAssert(String json) {
        objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule());

        apiError = parseJson(json)
    }

    public ErrorResponseAssert hasFieldWithErrorMsg(String fieldName, String containsMessage) {
        Assertions.assertThat(apiError.subErrors)
                .anyMatch({ ApiValidationError it ->
                    it.field == fieldName && it.message.containsIgnoreCase(containsMessage)
                })

        this
    }

    public ApiError parseJson(String jsonStr) {
        objectMapper.readValue(jsonStr, ApiError)
    }
}
