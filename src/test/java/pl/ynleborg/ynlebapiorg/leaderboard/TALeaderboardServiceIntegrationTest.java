package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TALeaderboardServiceIntegrationTest {

    @Autowired
    private TALeaderboardService service;

    @Test
    public void shouldStoreCurrentScoreToFile() throws IOException {
        service.storeCurrentScores();
    }

    @Test
    public void shouldGenerateDisplayableResult() throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(om.writeValueAsString(service.getDisplayableScores()));
    }

}