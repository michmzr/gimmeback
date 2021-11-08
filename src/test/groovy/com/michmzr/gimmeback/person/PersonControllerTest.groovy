package com.michmzr.gimmeback.person

import com.fasterxml.jackson.databind.ObjectMapper
import com.michmzr.gimmeback.RestBaseControllerSpec
import com.michmzr.gimmeback.StubSecurityConfig
import com.michmzr.gimmeback.item.Item
import com.michmzr.gimmeback.item.ItemDTO
import com.michmzr.gimmeback.item.ItemType
import com.michmzr.gimmeback.rest.ErrorResponseAssert
import com.michmzr.gimmeback.user.User
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.mock.DetachedMockFactory

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [PersonController])
@ContextConfiguration(classes = [StubSecurityConfig, StubConfig])
class PersonControllerTest extends RestBaseControllerSpec {
    private final static URL_PERSON_ENDPOINT = "/api/v1/person/"

    @Autowired
    protected MockMvc mvc

    @Autowired
    private PersonApiService personApiService

    @Autowired
    protected PersonMapper personMapper

    @Autowired
    private ObjectMapper objectMapper

    private def slurper = new JsonSlurper(type: JsonParserType.INDEX_OVERLAY)

    @WithMockUser(value = "basic")
    def "should return list of Persons"() {
        given:
        List<PersonDTO> persons = [
                person(1, "Johnny", "Good", "johhny.b.good", "123-456,789"),
                person(2, "Ned", "Stark", "ned.stark@stark.com", "555-980-213")
        ].collect { toDto(it) }

        personApiService.findAllByAuthor() >> persons

        expect:
        assertRestFindAll(mvc, URL_PERSON_ENDPOINT,  toJsonString(persons) )
    }

    @WithMockUser(value = "basic")
    def "when person created, expect 201 (Created)"() {
        given:
        def personDto = toDto(person(null, "Johnny", "Good",
                "johhny.b.good@com.pl", "123-456,789"))

        String body = objectMapper.writeValueAsString(personDto)

        personApiService.save(personDto) >> {
            personDto.id = 1
            personDto
        }

        expect:
        assertRestCreate(mvc, URL_PERSON_ENDPOINT,
                body, "http://localhost${URL_PERSON_ENDPOINT}1")
    }

    @WithMockUser(value = "basic")
    def "when create and request body contains validation errors, expect 400 (Bad Request)..."() {
        given:
        def personDto = new PersonDTO()
        String body = toJsonString(personDto)

        expect:
        assertRestCreateFailedValidation(mvc, URL_PERSON_ENDPOINT, body,[
                "name" :['must not be empty'],
                "surname": ['must not be empty']
        ] )
    }

    @WithMockUser(value = "basic")
    def "when Person exists expect to get Person DTO"() {
        given:
        def person = person(1)
        def personDto = toDto(person)

        personApiService.findAndDto(person.getId()) >> Optional.of(personDto)

        expect:
        assertRestGet(mvc, URL_PERSON_ENDPOINT  + person.getId(), toJsonString(personDto))
    }

    @WithMockUser(value = "basic")
    def "when finding Person and person not exists expect to get NO_CONTENT"() {
        expect:
        assertRestHasCode(mvc, URL_PERSON_ENDPOINT + "43", HttpStatus.NO_CONTENT)
    }

    @WithMockUser(value = "basic")
    def "when deleting and Person exists expect OK"() {
        given:
        def person = person(1)
        def personDto = toDto(person)
        personApiService.findAndDto(person.getId()) >> Optional.of(personDto)

        expect:
        assertRestDeleted(mvc, URL_PERSON_ENDPOINT + person.id)
    }

    @WithMockUser(value = "basic")
    def "when Person exists expect to update Item and get 200"() {
        given:
        def person = person(222)
        def updateItem = toDto(person)
        personApiService.find(person.getId()) >> Optional.of(person)

        expect:
        assertRestUpdate(mvc, URL_PERSON_ENDPOINT  + person.getId(), toJsonString(updateItem))
    }

    @WithMockUser(value = "basic")
    def "when Item not exists on update expect to get NO_CONTENT"() {
        given:
        def updateItem = toDto(person(2))

        expect:
        assertRestUpdateNotFound(mvc, URL_PERSON_ENDPOINT + "1231321", toJsonString(updateItem))
    }

    //todo update with validation error

    private Person person(Long id, String name = "John", String surname = "Tsunami",
                          String email = "john.tsunami.pl@x.com", String phone = "123-456-789") {
        new Person(id, name, surname, email, phone,
                new User("x", "y", email, "123456"))
    }

    private String toJsonString(def object) {
        objectMapper.writeValueAsString(object)
    }

    private PersonDTO toDto(Person person) {
        personMapper.toDTO(person)
    }

    private Person fromDto(PersonDTO personDto) {
        personMapper.fromDTO(personDto)
    }

    @TestConfiguration
    static class StubConfig {
        private DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        PersonApiService personApiService() {
            detachedMockFactory.Stub(PersonApiService)
        }

        @Bean
        PersonMapper personMapper() {
            new PersonMapperImpl()
        }
    }
}
