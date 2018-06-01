package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score {

    private String key;

    private String userName;

    private String platform;

    private String icon;

    private Long score;

    private Long tournamentPoints;
}
