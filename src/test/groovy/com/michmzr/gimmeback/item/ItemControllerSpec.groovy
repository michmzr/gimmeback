package com.michmzr.gimmeback.item

import com.michmzr.gimmeback.SpockIntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(controllers = [ItemController])
class ItemControllerSpec extends SpockIntegrationTest {
    @Autowired
    MockMvc mvc

    @Autowired
    ItemService itemService;
    ItemMapper itemMapper = new ItemMapperImpl()

    void setup() {
    }

    @WithMockUser(value = "spring")
    def "should return get user records"() {
        given:
            itemService.findAllByAuthor() >> [
                    new ItemDTO(id: 1, name: 'A', value: BigDecimal.ONE, type: ItemType.BOOK),
                    new ItemDTO(id: 2, name: 'B', value: BigDecimal.TEN, type: ItemType.BOOK),
            ]

        when:
            def response =
                    mvc.perform(
                            get('/api/v1/item/findAll').contentType(MediaType.APPLICATION_JSON)
                    ).andReturn().response

        then:
            response.status == 200
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        ItemService itemService() {
            return detachedMockFactory.Stub(ItemService)
        }
    }
}
