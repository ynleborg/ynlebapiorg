package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisplayableScore {

    private String userName;

    private String initialScore;

    private String currentScore;

    private Long delta;

    private String icon;
}
