package pl.ynleborg.ynlebapiorg.mac;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

@Component
@Slf4j
public class XboxApiClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String achievementsUrl;

    @Autowired
    XboxApiClient(
            @Value("${xboxapi.apikey}")
                    String apiKey,
            @Value("${xboxapi.v2.achievements.url}")
                    String achievementsUrl,
            WebClient.Builder webClient) {
        this.apiKey = apiKey;
        this.achievementsUrl = achievementsUrl;
        this.webClient = webClient.build();
    }

    @Cacheable("getAchievements")
    public Achievement[] getAchievements(Long xuid, Long titleId) {
        long start = System.currentTimeMillis();
        log.info(" > getAchievements xuid={}, titleId={}", xuid, titleId);
        Achievement[] block = webClient
                .method(HttpMethod.GET)
                .uri(uriBuilder -> createUri(xuid, titleId))
                .header("X-AUTH", apiKey)
                .exchange()
                .flatMap(clientResponse -> {
                    int statusCode = clientResponse.statusCode().value();
                    if (statusCode == 200) {
                        return clientResponse
                                .toEntity(Achievement[].class)
                                .map(result -> handleSuccessfulResponse(result, titleId));
                    } else {
                        return clientResponse
                                .toEntity(String.class)
                                .map(entity -> handleErrorResponse(statusCode, entity));
                    }
                }).block();
        log.info(" < getAchievements time={} ms", System.currentTimeMillis() - start);
        return block;
    }

    private Achievement[] handleSuccessfulResponse(HttpEntity<Achievement[]> entity, long titleId) {
        Achievement[] body = entity.getBody();
        if (body != null && titleId == 1909043648L) { // ugly! but api returns same platform for GearVR and Android...
            Arrays.stream(body).sequential().forEach(e -> e.setPlatforms(Collections.singletonList("GearVR")));
        }
        return body;
    }

    private Achievement[] handleErrorResponse(int statusCode, HttpEntity<String> entity) {
        throw new IllegalStateException(String.format("Invalid status code %s from xboxapi", statusCode), new IllegalArgumentException(entity.getBody()));
    }

    private URI createUri(long xuid, long titleId) {
        return UriComponentsBuilder
                .fromHttpUrl(achievementsUrl)
                .buildAndExpand(xuid, titleId)
                .toUri();
    }
}
