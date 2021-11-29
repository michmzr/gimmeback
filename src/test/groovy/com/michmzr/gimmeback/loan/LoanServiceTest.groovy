package com.michmzr.gimmeback.loan

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.michmzr.gimmeback.item.ItemMapper
import com.michmzr.gimmeback.item.ItemMapperImpl
import com.michmzr.gimmeback.security.SpringSecurityService
import spock.lang.Specification


class LoanServiceTest extends Specification {
    @Collaborator
    LoanRepository loanRepository = Mock(LoanRepository)

    @Collaborator
    SpringSecurityService springSecurityService = Mock(SpringSecurityService)

    @Collaborator
    LoanMapper loanMapper = new LoanMapperImpl()

    @Collaborator
    ItemMapper itemMapper = new ItemMapperImpl()

    @Subject
    LoanApiService loanService

    def setup() {
    }

    /*
    def "test find"() {
        given:

        when:

        then:
    }

    def "test save"() {
        given:

        when:
            // TODO implement stimulus
        then:
            // TODO implement assertions
    }

    def "test update"() {
        given:

        when:
            // TODO implement stimulus
        then:
            // TODO implement assertions
    }

    def "test delete"() {
        given:

        when:
            // TODO implement stimulus
        then:
            // TODO implement assertions
    }

    def "test resolve"() {
        given:

        when:
            // TODO implement stimulus
        then:
            // TODO implement assertions
    }

    */
}
