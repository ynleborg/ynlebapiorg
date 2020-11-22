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

    @GetMapping("/api/healthcheck")
    public Boolean healthcheck() {
        return Boolean.TRUE;
    }

    @GetMapping("/api/mac")
    public Collection<AchievementDto> getModel(@RequestParam String user, @RequestParam String[] platforms) {
        log.info(" > getModel user={}, platforms={}", user, platforms);
        Collection<AchievementDto> model = service.getModel();
        log.info(" < getModel model={}", model);
        return model;
    }
}
