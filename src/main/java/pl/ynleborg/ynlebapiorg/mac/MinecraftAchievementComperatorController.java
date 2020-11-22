package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class MinecraftAchievementComperatorController {

    private MinecraftAchievementComparatorService service;

    @GetMapping("/api/healthcheck")
    public Boolean healthcheck() {
        return Boolean.TRUE;
    }

    @GetMapping("/api/mac")
    public Collection<AchievementDto> getModel() {
        log.info(" > getModel");
        Collection<AchievementDto> model = service.getModel();
        log.info(" < getModel model={}", model);
        return model;
    }
}
