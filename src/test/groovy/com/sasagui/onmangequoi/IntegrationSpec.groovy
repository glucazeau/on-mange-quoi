package com.sasagui.onmangequoi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@ActiveProfiles("integration-test")
@SpringBootTest
@AutoConfigureMockMvc
@Stepwise
abstract class IntegrationSpec extends Specification {

    static PostgreSQLContainer postgresql = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("brand-portal")
            .withUsername("brand-portal")
            .withPassword("brand-portal")

    @Shared
    PostgreSQLContainer postgresqlContainer = postgresql

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresql::getJdbcUrl)
        registry.add("spring.datasource.username", postgresql::getUsername)
        registry.add("spring.datasource.password", postgresql::getPassword)
    }

    @Autowired
    protected MockMvc mvc

    def setupSpec() {
        postgresqlContainer.start()
    }

    def cleanupSpec() {
        postgresqlContainer.stop()
    }

}
