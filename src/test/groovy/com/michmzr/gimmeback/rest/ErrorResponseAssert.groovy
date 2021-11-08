package com.michmzr.gimmeback.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions

import java.util.stream.Collectors

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

    public ErrorResponseAssert hasFieldContainsFieldErrors(String fieldName, List<String> expectedValidationErrors) {
        assert apiError.subErrors.find(it -> it.field.equals(fieldName))

        List<String> actualValidationErrors = apiError
                .subErrors
                .stream()
                .filter(it -> it.field == fieldName)
                .map(it -> it.message)
                .collect(Collectors.toList())

        Assertions
                .assertThat(actualValidationErrors)
                .containsAll(expectedValidationErrors)

        this
    }

    public ApiError parseJson(String jsonStr) {
        objectMapper.readValue(jsonStr, ApiError)
    }
}
