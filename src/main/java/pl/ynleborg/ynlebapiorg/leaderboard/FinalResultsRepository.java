package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class FinalResultsRepository {
    private final ObjectMapper om = new ObjectMapper();

    public List<DisplayableScore> getLeaderboardFinal() throws IOException {
        return om.readValue(new File("leaderboard_final.json"), new TypeReference<List<DisplayableScore>>() {
        });
    }

    public List<DisplayableScore> getLeaderboardCombinedFinal() throws IOException {
        return om.readValue(new File("leaderboardcombined_final.json"), new TypeReference<List<DisplayableScore>>() {
        });
    }
}
