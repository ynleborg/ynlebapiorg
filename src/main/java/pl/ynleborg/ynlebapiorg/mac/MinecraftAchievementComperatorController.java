package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class MinecraftAchievementComperatorController {

    private final MinecraftAchievementComparatorService service;

    private static final Platform[] gameIds = {Platform.X, Platform.W, Platform.A, Platform.G, Platform.N, Platform.I};

    @GetMapping("/api/healthcheck")
    public Boolean healthcheck() {
        return Boolean.TRUE;
    }

    @GetMapping("/api/mac")
    public Collection<AchievementDto> getModel(@RequestParam String gamertag, @RequestParam String[] platforms) {
        log.info(" > getModel gamertag={}, platforms={}", gamertag, platforms);
        Collection<AchievementDto> model = service.getModel(gamertag, gameIds);
        log.info(" < getModel model={}", model);
        return model;
    }
}
