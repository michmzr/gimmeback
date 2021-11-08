package com.michmzr.gimmeback

import com.michmzr.gimmeback.rest.ErrorResponseAssert
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class RestBaseControllerSpec extends Specification {
    public void assertRestCreate(MockMvc mvc, String endpointUrl, String body, String expectedLocation) {
        mvc.perform(
                post(endpointUrl)
                        .contentType(APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        HttpHeaders.LOCATION, expectedLocation))
    }

    public void assertRestFindAll(MockMvc mvc, String endpointUrl, String expectedResponseBody) {

        mvc
                .perform(get(endpointUrl).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        expectedResponseBody
                ))
    }

    public void assertRestGet(MockMvc mvc, String endpointUrl, String expectedResponseBody) {
        mvc.perform(
                get(endpointUrl).contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().json(
                        expectedResponseBody
                ))
    }

    public void assertRestHasCode(MockMvc mvc, String endpointUrl, HttpStatus code) {
        mvc.perform(
                get(endpointUrl).contentType(APPLICATION_JSON)
        ).andExpect(status().is(code.value()))
                .andReturn()
    }

    public void assertRestHasCode(MockMvc mvc, String httpMethod, String endpointUrl, HttpStatus code) {
        mvc.perform(
                MockMvcRequestBuilders.request(httpMethod, endpointUrl).contentType(APPLICATION_JSON)
        ).andExpect(status().is(code.value()))
                .andReturn()
    }

    public void assertRestDeletedNotContent(MockMvc mvc, String endpointUrl) {
        mvc.perform(
                delete(endpointUrl)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent())
                .andReturn()
    }

    public void assertRestDeleted(MockMvc mvc, String endpointUrl) {
        mvc.perform(
                delete(endpointUrl)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn()
    }

    public void assertRestCreateFailedValidation(MockMvc mvc, String endpointUrl, String body, Map<String, List<String>> fieldMsgs) {
        def requestBody = mvc.perform(
                post(endpointUrl).contentType(APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().response.getContentAsString()

        ErrorResponseAssert errorResponseAssert = new ErrorResponseAssert(requestBody)

        fieldMsgs.each { fieldName, msgs ->
            assert errorResponseAssert.hasFieldContainsFieldErrors(fieldName, msgs)
        }
    }

    public void assertRestUpdate(MockMvc mvc, String endpointUrl, String body) {
        mvc.perform(
                put(endpointUrl).contentType(APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isOk())
    }

    public void assertRestUpdateNotFound(MockMvc mvc, String endpointUrl, String body) {
        mvc.perform(
                put(endpointUrl).contentType(APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isNoContent())
    }
}
