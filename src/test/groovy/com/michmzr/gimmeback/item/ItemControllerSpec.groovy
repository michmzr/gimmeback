package com.michmzr.gimmeback.item

import com.fasterxml.jackson.databind.ObjectMapper
import com.michmzr.gimmeback.RestBaseControllerSpec
import com.michmzr.gimmeback.StubSecurityConfig
import com.michmzr.gimmeback.rest.ErrorResponseAssert
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.mock.DetachedMockFactory

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [ItemController])
@ContextConfiguration(classes = [StubSecurityConfig, StubConfig])
class ItemControllerSpec extends RestBaseControllerSpec {
    private static final String URL_ITEM_ENDPOINT = "/api/v1/item/"

    @Autowired
    private MockMvc mvc

    @Autowired
    private ItemApiService itemService

    @Autowired
    private  ItemMapper itemMapper

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
                get(URL_ITEM_ENDPOINT).contentType(APPLICATION_JSON)
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

        itemService.save(itemDTO) >> {
            itemDTO.id = 1
            itemDTO
        }

        expect:
        assertRestCreate(mvc, URL_ITEM_ENDPOINT, body, "http://localhost${URL_ITEM_ENDPOINT}1")
    }

    @WithMockUser(value = "basic")
    def "when Item exists expect to get Item DTO"() {
        given:
        def item = new Item(id: 1, name: 'Item name', value: 5.8, type: ItemType.BOOK)
        itemService.find(item.getId()) >> Optional.of(item)

        expect:
        assertRestGet(mvc, URL_ITEM_ENDPOINT + item.getId(),  toJsonString(toDto(item)))
    }

    @WithMockUser(value = "basic")
    def "when Item not exists expect to get NO_CONTENT"() {
        expect:
        assertRestHasCode(mvc, URL_ITEM_ENDPOINT + "4324234234", HttpStatus.NO_CONTENT)
    }

    @WithMockUser(value = "basic")
    def "when deleting and Item exists expect item deleted"() {
        given:
        def item = new Item(id: 1, name: 'Item name', value: 5.8, type: ItemType.BOOK)
        itemService.find(item.getId()) >> Optional.of(item)
        expect:
        assertRestDeleted(mvc, URL_ITEM_ENDPOINT + item.getId())
    }

    @WithMockUser(value = "basic")
    def "when deleting and Item not exists expect NO_CONTENT"() {
        expect:
        assertRestDeletedNotContent(mvc,  "/api/v1/item/4534")
    }

    @WithMockUser(value = "basic")
    def "when create and request body contains validation errors, expect 400 (Bad Request)..."() {
        given:

        ItemDTO itemDTO = new ItemDTO(value: null)
        String body = toJsonString(itemDTO)

        when:
        def request = mvc.perform(
                post(URL_ITEM_ENDPOINT).contentType(APPLICATION_JSON).content(body)
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

    @WithMockUser(value = "basic")
    def "when Item exists expect to update Item and get 200"() {
        given:
        def item = new Item(id: 1, name: 'Item name', value: 5.8, type: ItemType.BOOK)
        itemService.find(item.getId()) >> Optional.of(item)

        def updateItem = new ItemDTO(name: 'New name', value: 1, type: ItemType.DEVICE)

        expect:
        assertRestUpdate(mvc, URL_ITEM_ENDPOINT + item.getId(),toJsonString(updateItem) )
    }

    @WithMockUser(value = "basic")
    def "when Item not exists on update expect to get NO_CONTENT"() {
        given:
        def updateItem = new ItemDTO(name: 'New name', value: 1, type: ItemType.DEVICE)
        expect:
        assertRestUpdateNotFound(mvc, URL_ITEM_ENDPOINT + "43",toJsonString(updateItem))
    }

    //todo update with validation error

    private ItemDTO toDto(Item item) {
        itemMapper.toDTO(item)
    }

    private String toJsonString(def item) {
        objectMapper.writeValueAsString(item)
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        ItemApiService itemService() {
            detachedMockFactory.Stub(ItemApiService)
        }

        @Bean
        ItemMapper itemMapper() {
            new ItemMapperImpl()
        }
    }
}
