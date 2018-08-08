package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class ScoreRepository {
    private final ObjectMapper om = new ObjectMapper();

    public void storeScores(List<InitialScore> initialScores) throws IOException {
        om.enable(SerializationFeature.INDENT_OUTPUT);
        om.writeValue(new File("store" + System.currentTimeMillis() + ".json"), initialScores);
    }

    public List<InitialScore> getInitialScores() throws IOException {
        return om.readValue(new File("store.json"), new TypeReference<List<InitialScore>>() {
        });
    }
}
