package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TALeaderboardService {

    @Autowired
    private TAClient taClient;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private FinalResultsRepository finalResultsRepository;

    private Boolean finalStage = true;


    public void storeCurrentScores() throws IOException {
        scoreRepository.storeScores(taClient.getScores());
    }

    public List<DisplayableScore> getDisplayableScores() throws IOException {
        return finalStage ?
                finalResultsRepository.getLeaderboardFinal() :
                getDisplayableScoresInternal().stream()
                        .sorted(Comparator.comparing(DisplayableScore::getTotal).reversed())
                        .collect(Collectors.toList());
    }

    public List<DisplayableScore> getCombinedDisplayableScores() throws IOException {
        return finalStage ?
                finalResultsRepository.getLeaderboardCombinedFinal() :
                flattened(getDisplayableScoresInternal()).stream()
                        .sorted(Comparator.comparing(DisplayableScore::getTotal).reversed())
                        .collect(Collectors.toList());
    }

    private List<DisplayableScore> getDisplayableScoresInternal() throws IOException {
        List<InitialScore> initialInitialScores = scoreRepository.getInitialScores();

        List<InitialScore> currentInitialScores = taClient.getScores();
        List<DisplayableScore> result = new ArrayList<>();
        currentInitialScores.forEach(current -> {
            InitialScore initial = initialInitialScores.stream().filter(is -> is.getKey().equals(current.getKey())).findFirst().orElse(current);
            result.add(DisplayableScore.fromScore(initial, current.getScore()));
        });
        return result;
    }


    private List<DisplayableScore> flattened(List<DisplayableScore> initialScores) {
        List<DisplayableScore> flattened = new ArrayList<>();
        initialScores.forEach(s -> {
            DisplayableScore candidate = flattened.stream()
                    .filter(is -> is.getUserName().substring(0, 4).toUpperCase().equals(s.getUserName().substring(0, 4).toUpperCase()))
                    .findFirst()
                    .orElse(null);
            if (candidate == null) {
                flattened.add(copy(s));
            } else {
                candidate.setTournamentPoints(candidate.getTournamentPoints() + s.getTournamentPoints());
                candidate.setInitialScore(candidate.getInitialScore() + s.getInitialScore());
                candidate.setCurrentScore(candidate.getCurrentScore() + s.getCurrentScore());
                candidate.setDelta(candidate.getDelta() + s.getDelta());
                candidate.setTotal(candidate.getTotal() + s.getTotal());
            }
        });
        return flattened;
    }

    public DisplayableScore copy(DisplayableScore score) {
        return DisplayableScore.builder()
                .userName(score.getUserName())
                .platform(null)
                .icon(score.getIcon())
                .tournamentPoints(score.getTournamentPoints())
                .initialScore(score.getInitialScore())
                .currentScore(score.getCurrentScore())
                .delta(score.getDelta())
                .total(score.getTotal())
                .build();

    }
}
