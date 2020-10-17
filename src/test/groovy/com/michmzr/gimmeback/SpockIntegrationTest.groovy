package com.michmzr.gimmeback

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [GimmeBackApplication.class])
//@ActiveProfiles("IT")
abstract class SpockIntegrationTest extends Specification {
    @LocalServerPort
    protected int port;
}
