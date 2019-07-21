package pl.ynleborg.ynlebapiorg.leaderboard;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TAClientIntegrationTest {

    @Autowired
    private TAClient client;

    @Test
    public void shouldScrapFromLeaderboard() throws IOException {
        //when
        List<InitialScore> initialScores = client.getScores();

        //then
        Assertions.assertThat(initialScores).isNotEmpty();
    }
}