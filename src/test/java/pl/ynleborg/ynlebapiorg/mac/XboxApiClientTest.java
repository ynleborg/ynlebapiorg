package pl.ynleborg.ynlebapiorg.mac;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@WebFluxTest
class XboxApiClientTest {
    @Autowired
    private XboxApiClient client;

    @Autowired
    private MockWebServer mockWebServer;

    @AfterAll
    void afterClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void should_parse_response() {
        // given
        String textBodyResponse = new Scanner(getClass().getResourceAsStream("xboxapi_achievements.json"), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(textBodyResponse));

        // when
        Achievement[] response = client.getAchievements(123, 456);

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(107);
    }

    @Configuration
    static class TestConfig {
        @Bean
        MockWebServer mockWebServer() {
            return new MockWebServer();
        }

        @Bean
        WebClient.Builder webClientBuilder() {
            return mock(WebClient.Builder.class);
        }

        @Bean
        @Primary
        XboxApiClient xboxApiClient(MockWebServer mockWebServer) {
            return new XboxApiClient("secretapikey", mockWebServer.url("/").toString(), WebClient.builder());
        }
    }

}
