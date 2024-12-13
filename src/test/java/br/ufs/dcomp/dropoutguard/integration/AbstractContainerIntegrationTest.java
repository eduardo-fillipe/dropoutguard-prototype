package br.ufs.dcomp.dropoutguard.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractContainerIntegrationTest {

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:12.13-alpine")
    );

    static RabbitMQContainer rabbitContainer = new RabbitMQContainer(
            DockerImageName.parse("rabbitmq:4.0.3-alpine")
    );

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        rabbitContainer.start();

        registry.add("spring.rabbitmq.host", rabbitContainer::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitContainer.getMappedPort(5672));

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}
