package pl.ynleborg.ynlebapiorg.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
public class TALeaderboardController {

    @Autowired
    private TALeaderboardService service;

    @RequestMapping("/api/leaderboard")
    public Collection<DisplayableScore> leaderboard(HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:63342");
        return service.getDisplayableScores();
    }


}
