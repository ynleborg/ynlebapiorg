package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;


public class TALeaderboardServiceTest {

    TALeaderboardService service = new TALeaderboardService();

    @Test
    public void test() throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(om.writeValueAsString(service.getModel()));
    }
}