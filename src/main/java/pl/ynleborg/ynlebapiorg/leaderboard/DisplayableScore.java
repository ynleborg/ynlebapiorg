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

    private Long initialScore;

    private Long currentScore;

    private Long delta;

    private Long total;

    public String getKey() {
        return userName + (platform != null ? '/' + platform : "");
    }

    public static DisplayableScore fromScore(InitialScore initial, Long currentScore) {
        long delta = calculateDelta(initial, currentScore);
        return DisplayableScore.builder()
                .userName(initial.getUserName())
                .platform(initial.getPlatform())
                .icon(initial.getIcon())
                .tournamentPoints(initial.getTournamentPoints())
                .initialScore(initial.getScore())
                .currentScore(currentScore)
                .delta(delta)
                .total(delta + initial.getTournamentPoints())
                .build();
    }

    private static long calculateDelta(InitialScore initial, Long currentScore) {
        return (long) ((currentScore - initial.getScore()) * platformRatio(initial.getPlatform()));
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
