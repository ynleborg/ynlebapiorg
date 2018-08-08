package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisplayableScore {

    private String userName;

    private String platform;

    private String icon;

    private Long tournamentPoints;

    private String initialScore;

    private String currentScore;

    private Long delta;

    public String getKey() {
        return userName + '/' + platform;
    }

    public static DisplayableScore fromScore(Score initial, Long currentScore) {
        return DisplayableScore.builder()
                .userName(initial.getUserName())
                .platform(initial.getPlatform())
                .icon(initial.getIcon())
                .tournamentPoints(initial.getTournamentPoints())
                .initialScore(NumberFormat.getNumberInstance(Locale.US).format(initial.getScore()))
                .currentScore(NumberFormat.getNumberInstance(Locale.US).format(currentScore))
                .delta(calculateDelta(initial, currentScore))
                .build();
    }

    private static long calculateDelta(Score initial, Long currentScore) {
        return (long) ((currentScore - initial.getScore() + initial.getTournamentPoints()) * platformRatio(initial.getPlatform()));
    }

    private static double platformRatio(String platform) {
        if ("steam".equals(platform)) {
            return 4.0;
        } else if ("ps4".equals(platform)) {
            return 1.3;
        } else {
            return 1.0;
        }
    }
}
