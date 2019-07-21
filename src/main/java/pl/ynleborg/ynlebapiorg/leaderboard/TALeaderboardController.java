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
        decorate(response);
        return service.getDisplayableScores();
    }

    @RequestMapping("/api/leaderboardcombined")
    public Collection<DisplayableScore> leaderboardCombined(HttpServletResponse response) throws IOException {
        decorate(response);
        return service.getCombinedDisplayableScores();
    }

    private void decorate(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
