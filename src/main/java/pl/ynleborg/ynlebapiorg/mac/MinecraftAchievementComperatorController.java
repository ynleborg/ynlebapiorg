package pl.ynleborg.ynlebapiorg.mac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@RestController
public class MinecraftAchievementComperatorController {

    @Autowired
    private MinecraftAchievementComperatorService service;

    @RequestMapping("/api/healthcheck")
    public Boolean healthcheck() {
        return Boolean.TRUE;
    }

    //kolejnosc flag xbox;win10;android;gearvr;piotr
    @RequestMapping("/api/mac")
    public Collection<Achievement> getModel() throws IOException {
        return service.getModel();
    }


}
