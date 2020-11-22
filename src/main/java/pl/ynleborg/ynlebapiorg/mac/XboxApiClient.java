package pl.ynleborg.ynlebapiorg.mac;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
public class XboxApiClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String achievementsUrl;
    private final String gamertagUrl;

    @Autowired
    XboxApiClient(
            @Value("${xboxapi.apikey}")
                    String apiKey,
            @Value("${xboxapi.v2.achievements.url}")
                    String achievementsUrl,
            @Value("${xboxapi.v2.gamertag.url}")
                    String gamertagUrl,
            WebClient.Builder webClient) {
        this.apiKey = apiKey;
        this.achievementsUrl = achievementsUrl;
        this.gamertagUrl = gamertagUrl;
        this.webClient = webClient.build();
    }

    @Cacheable(value = "getAchievements", cacheManager = "getAchievementsCM")
    public Achievement[] getAchievements(Long xuid, Long titleId) {
        long start = System.currentTimeMillis();
        log.info(" > getAchievements xuid={}, titleId={}", xuid, titleId);
        Achievement[] achievements = webClient
                .method(HttpMethod.GET)
                .uri(uriBuilder -> createUri(xuid, titleId))
                .header("X-AUTH", apiKey)
                .exchange()
                .flatMap(clientResponse -> {
                    int statusCode = clientResponse.statusCode().value();
                    outputQuota(clientResponse);
                    if (statusCode == 200) {
                        return clientResponse
                                .toEntity(Achievement[].class)
                                .map(HttpEntity::getBody);
                    } else {
                        return clientResponse
                                .toEntity(String.class)
                                .map(entity -> handleErrorResponse(statusCode, entity));
                    }
                }).block();
        log.info(" < getAchievements size={} time={} ms", achievements != null ? achievements.length : null, System.currentTimeMillis() - start);
        return achievements;
    }

    private void outputQuota(ClientResponse clientResponse) {
        log.info("Quota Limit={}/Remaining={}/Reset={}", clientResponse.headers().header("X-RateLimit-Limit"), clientResponse.headers().header("X-RateLimit-Remaining"), clientResponse.headers().header("X-RateLimit-Reset"));
    }

    @Cacheable(value = "getXuid", cacheManager = "getXuidCM")
    public Long getXuid(String gamertag) {
        long start = System.currentTimeMillis();
        log.info(" > getXuid gamertag={}", gamertag);
        String xuid = (String) webClient
                .method(HttpMethod.GET)
                .uri(uriBuilder -> createUri2(gamertag))
                .header("X-AUTH", apiKey)
                .exchange()
                .flatMap(clientResponse -> {
                    int statusCode = clientResponse.statusCode().value();
                    if (statusCode == 200) {
                        return clientResponse
                                .toEntity(String.class)
                                .map(HttpEntity::getBody);
                    } else {
                        return clientResponse
                                .toEntity(String.class)
                                .map(entity -> handleLongErrorResponse(statusCode, entity));
                    }
                }).block();
        log.info(" < getXuid xuid={} time={} ms", xuid, System.currentTimeMillis() - start);
        return Long.valueOf(xuid);
    }

    private Achievement[] handleErrorResponse(int statusCode, HttpEntity<String> entity) {
        throw new IllegalStateException(String.format("Invalid status code %s from xboxapi", statusCode), new IllegalArgumentException(entity.getBody()));
    }

    private Long handleLongErrorResponse(int statusCode, HttpEntity<String> entity) {
        throw new IllegalStateException(String.format("Invalid status code %s from xboxapi", statusCode), new IllegalArgumentException(entity.getBody()));
    }

    private URI createUri(long xuid, long titleId) {
        return UriComponentsBuilder
                .fromHttpUrl(achievementsUrl)
                .buildAndExpand(xuid, titleId)
                .toUri();
    }

    private URI createUri2(String gamertag) {
        return UriComponentsBuilder
                .fromHttpUrl(gamertagUrl)
                .buildAndExpand(gamertag)
                .toUri();
    }
}
