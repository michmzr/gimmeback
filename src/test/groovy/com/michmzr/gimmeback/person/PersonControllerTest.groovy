package com.michmzr.gimmeback.person

import com.fasterxml.jackson.databind.ObjectMapper
import com.michmzr.gimmeback.StubSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@WebMvcTest(controllers = [PersonController])
@ContextConfiguration(classes = [StubSecurityConfig])
class PersonControllerTest extends Specification {
    private final static CONTROLLER_ENDPOINT = "/api/v1/person/"

    @MockBean
    private PersonApiService personApiService

    @Autowired
    protected MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @WithMockUser(value = "basic")
    def "should return list of Persons"() {
        given:
        personApiService.findAll() >> [
                person(),
                person(2, "Ned", "Stark", "ned.stark@stark.com", "555-980-213")
        ]
        when:
        def results = mvc.perform(
                MockMvcRequestBuilders
                        .get(CONTROLLER_ENDPOINT)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
        then:
        HttpStatus.valueOf(results.status) == HttpStatus.OK

    }

    PersonDTO person(Long id = 1, String name = "John", String surname = "Tsunami", String email = "john.tsunami.pl",
                     String phone = "123-456-789") {
        new PersonDTO(id, name, surname, email, phone);
    }
}
