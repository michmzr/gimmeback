package com.michmzr.gimmeback

import com.michmzr.gimmeback.security.CustomUserDetailsService
import com.michmzr.gimmeback.security.JwtAuthenticationEntryPoint
import com.michmzr.gimmeback.security.JwtTokenProvider
import com.michmzr.gimmeback.security.UserPrincipal
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@TestConfiguration
public class StubSecurityConfig {
    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint

    @MockBean
    JwtTokenProvider jwtTokenProvider

    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext

    @MockBean
    CustomUserDetailsService customUserDetailsService

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        UserPrincipal basicActiveUser = new UserPrincipal(1, "Basic", "basic", "basic@user.pl", "pass1234", Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
        ));

        return new InMemoryUserDetailsManager(Arrays.asList(
                basicActiveUser
        ));
    }
}