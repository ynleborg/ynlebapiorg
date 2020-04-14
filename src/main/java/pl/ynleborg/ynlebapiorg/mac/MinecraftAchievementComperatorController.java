package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class MinecraftAchievementComperatorController {

    private MinecraftAchievementComperatorService service;

    @GetMapping("/api/healthcheck")
    public Boolean healthcheck() {
        return Boolean.TRUE;
    }

    //kolejnosc flag xbox;win10;android;gearvr;piotr
    @GetMapping("/api/mac")
    public Collection<Achievement> getModel() throws IOException {
        log.info(" > getModel");
        Collection<Achievement> model = service.getModel();
        log.info(" < getModel model={}", model);
        return model;
    }


}
