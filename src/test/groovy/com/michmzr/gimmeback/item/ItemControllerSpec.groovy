package com.michmzr.gimmeback.item

import com.fasterxml.jackson.databind.ObjectMapper
import com.michmzr.gimmeback.StubSecurityConfig
import com.michmzr.gimmeback.rest.ErrorResponseAssert
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [ItemController])
@ContextConfiguration(classes = [StubSecurityConfig, StubConfig])
class ItemControllerSpec extends Specification {
    @Autowired
    MockMvc mvc

    @Autowired
    ItemService itemService

    @Autowired
    private ObjectMapper objectMapper

    private def slurper = new JsonSlurper(type: JsonParserType.INDEX_OVERLAY)

    @WithMockUser(value = "basic")
    def "should return list of user records"() {
        given:
        List items = [
                new Item(id: 1, name: 'A', value: BigDecimal.ONE, type: ItemType.BOOK),
                new Item(id: 2, name: 'B', value: BigDecimal.TEN, type: ItemType.BOOK),
        ]

        itemService.findAllByAuthor() >> items

        expect:
        mvc.perform(
                get('/api/v1/item/').contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))
                .andExpect(jsonPath('$[0].id', is(1)))
                .andExpect(jsonPath('$[0].name', is("A")))
                .andExpect(jsonPath('$[0].value', is(1)))
                .andExpect(jsonPath('$[0].type', is("BOOK")))
                .andExpect(jsonPath('$[1].id', is(2)))
                .andExpect(jsonPath('$[1].name', is("B")))
                .andExpect(jsonPath('$[1].value', is(10)))
                .andExpect(jsonPath('$[1].type', is("BOOK")))

    }


    @WithMockUser(value = "basic")
    def "when create and item created, expect 201 (Created)"() {
        given:
        ItemDTO itemDTO = new ItemDTO(name: "Book", type: ItemType.BOOK)
        String body = objectMapper.writeValueAsString(itemDTO)

        itemService.save(itemDTO) >> new Item(id: 1)

        when:
        def response =
                mvc.perform(
                        post('/api/v1/item/')
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
        then:
        response
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/v1/item/1"))
        and:
        response.andReturn().getResponse().with {
            getContentAsString() == "what you expect"
            getHeader(HttpHeaders.LOCATION).contains("/item/1")
        }
    }

    @WithMockUser(value = "basic")
    def "when create and request body contains validation errors, expect 400 (Bad Request)..."() {
        given:

        ItemDTO itemDTO = new ItemDTO(value: null)
        String body = objectMapper.writeValueAsString(itemDTO)

        when:
        def request = mvc.perform(
                post('/api/v1/item/').contentType(MediaType.APPLICATION_JSON).content(body)
        )

        then:
        request.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
        then:
        ErrorResponseAssert errorResponseAssert = new ErrorResponseAssert(request.andReturn().getResponse().getContentAsString())
        errorResponseAssert.hasFieldWithErrorMsg('name', 'not be null')
        errorResponseAssert.hasFieldWithErrorMsg('name', 'empty')
        errorResponseAssert.hasFieldWithErrorMsg('type', 'must not be null')
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        ItemService itemService() {
            detachedMockFactory.Stub(ItemService)
        }

        @Bean
        ItemMapper itemMapper() {
            new ItemMapperImpl()
        }
    }
}
