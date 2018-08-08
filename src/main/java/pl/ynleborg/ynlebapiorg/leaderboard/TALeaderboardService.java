package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TALeaderboardService {

    @Autowired
    private TAClient taClient;

    @Autowired
    private ScoreRepository scoreRepository;


    public void storeCurrentScores() throws IOException {
        scoreRepository.storeScores(taClient.getScores());
    }

    public List<DisplayableScore> getDisplayableScores() throws IOException {
        List<Score> initialScores = scoreRepository.getInitialScores();

        List<Score> currentScores = taClient.getScores();
        List<DisplayableScore> result = new ArrayList<>();
        currentScores.forEach(current -> {
            Score initial = initialScores.stream().filter(is -> is.getKey().equals(current.getKey())).findFirst().orElse(current);
            result.add(DisplayableScore.builder()
                    .userName(current.getUserName())
                    .icon(current.getIcon())
                    .initialScore(NumberFormat.getNumberInstance(Locale.US).format(initial.getScore()))
                    .currentScore(NumberFormat.getNumberInstance(Locale.US).format(current.getScore()))
                    .tournamentPoints(initial.getTournamentPoints())
                    .delta(calculateDelta(current, initial, current.getPlatform()))
                    .build());
        });
        return result.stream().sorted(Comparator.comparing(DisplayableScore::getDelta).reversed()).collect(Collectors.toList());
    }

    private long calculateDelta(Score current, Score initial, String platform) {
        return (long) ((current.getScore() - initial.getScore() + initial.getTournamentPoints()) * platformRatio(platform));
    }

    private double platformRatio(String platform) {
        if ("steam".equals(platform)) {
            return 4.0;
        } else if ("ps4".equals(platform)) {
            return 1.3;
        } else {
            return 1.0;
        }
    }
}
