package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Score {

    private String userName;

    private String platform;

    private String icon;

    private String date;

    private String score;

}
