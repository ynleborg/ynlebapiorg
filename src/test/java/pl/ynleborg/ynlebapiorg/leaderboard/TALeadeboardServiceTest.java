package pl.ynleborg.ynlebapiorg.leaderboard;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TALeadeboardServiceTest {

    @Mock
    private TAClient taClient;

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    public TALeaderboardService taLeaderboardService;

    @Test
    public void shouldReturnDisplayableScores() throws IOException {
        //given
        when(scoreRepository.getInitialScores()).thenReturn(initialScores());
        when(taClient.getScores()).thenReturn(currentScores());

        //when
        List<DisplayableScore> displayableScores = taLeaderboardService.getDisplayableScores();

        //then
        assertThat(displayableScores).isNotEmpty().hasSize(4);
        assertThat(find(displayableScores, "Ynleborg").getInitialScore()).isEqualTo("100");
        assertThat(find(displayableScores, "Ynleborg").getCurrentScore()).isEqualTo("1,000");
        assertThat(find(displayableScores, "Ynleborg").getTournamentPoints()).isEqualTo(10);
        assertThat(find(displayableScores, "Ynleborg").getDelta()).isEqualTo(910);

        assertThat(find(displayableScores, "Porkite").getInitialScore()).isEqualTo("300");
        assertThat(find(displayableScores, "Porkite").getCurrentScore()).isEqualTo("3,000");
        assertThat(find(displayableScores, "Porkite").getTournamentPoints()).isEqualTo(30);
        assertThat(find(displayableScores, "Porkite").getDelta()).isEqualTo(2730);

    }

    private DisplayableScore find(List<DisplayableScore> scores, String userName) {
        return scores.stream().filter(s -> s.getUserName().equals(userName)).findFirst().orElse(null);
    }

    private List<Score> initialScores() {
        return Arrays.asList(
                Score.builder().key("Ynleborg/xbox").userName("Ynleborg").platform("xbox").score(100L).tournamentPoints(10L).build(),
                Score.builder().key("ynleborg/steam").userName("ynleborg").platform("steam").score(200L).tournamentPoints(20L).build(),
                Score.builder().key("Porkite/xbox").userName("Porkite").platform("xbox").score(300L).tournamentPoints(30L).build());
    }

    private List<Score> currentScores() {
        return Arrays.asList(
                Score.builder().key("Ynleborg/xbox").userName("Ynleborg").platform("xbox").score(1000L).tournamentPoints(0L).build(),
                Score.builder().key("ynleborg/steam").userName("ynleborg").platform("steam").score(2000L).tournamentPoints(0L).build(),
                Score.builder().key("Porkite/xbox").userName("Porkite").platform("xbox").score(3000L).tournamentPoints(0L).build(),
                Score.builder().key("Porkite/steam").userName("Porkite").platform("steam").score(4000L).tournamentPoints(0L).build());
    }
}
