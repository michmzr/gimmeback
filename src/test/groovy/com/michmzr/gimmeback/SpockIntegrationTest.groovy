package com.michmzr.gimmeback


import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
//@ActiveProfiles("IT")
abstract class SpockIntegrationTest extends Specification {
    @LocalServerPort
    protected int port;
}
