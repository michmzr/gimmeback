package com.michmzr.gimmeback.item

import com.michmzr.gimmeback.StubSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(controllers = [ItemController])
@ContextConfiguration(classes = [StubSecurityConfig, StubConfig])
class ItemControllerSpec extends Specification {
    @Autowired
    MockMvc mvc

    @Autowired
    ItemService itemService;

    void setup() {
    }

    @WithMockUser(value = "basic")
    def "should return get user records"() {
        given:
        itemService.findAllByAuthor() >> [
                new Item(id: 1, name: 'A', value: BigDecimal.ONE, type: ItemType.BOOK),
                new Item(id: 2, name: 'B', value: BigDecimal.TEN, type: ItemType.BOOK),
        ]

        when:
        def response =
                mvc.perform(
                        get('/api/v1/item/').contentType(MediaType.APPLICATION_JSON)
                ).andReturn().response
        then:
        HttpStatus.valueOf(response.status) == HttpStatus.OK
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
