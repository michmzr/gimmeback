package com.michmzr.gimmeback.person

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest(controllers = [PersonController])
class PersonControllerTest extends Specification {
    @Autowired
    protected MockMvc mvc

    PersonApiService personApiService = Mock()

/*    def "should return empty list of persons when There is no in db"() {
        given:
        personApiService.findAll() >> []
        when
        def results
    }*/
}
