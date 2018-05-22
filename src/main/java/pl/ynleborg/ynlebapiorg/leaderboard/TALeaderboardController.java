package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@RestController
public class TALeaderboardController {

    @Autowired
    private TALeaderboardService service;

    @RequestMapping("/api/leaderboard")
    public String leaderboard() throws IOException {
        Collection<Score> model = service.getModel();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(model);
    }


}
