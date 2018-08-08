package pl.ynleborg.ynlebapiorg.leaderboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

        assertThat(find(displayableScores, "Ynleborg/xbox").getInitialScore()).isEqualTo(100);
        assertThat(find(displayableScores, "Ynleborg/xbox").getCurrentScore()).isEqualTo(1000);
        assertThat(find(displayableScores, "Ynleborg/xbox").getTournamentPoints()).isEqualTo(10);
        assertThat(find(displayableScores, "Ynleborg/xbox").getDelta()).isEqualTo(910); //ratio 1.0

        assertThat(find(displayableScores, "ynleborg/steam").getInitialScore()).isEqualTo(200);
        assertThat(find(displayableScores, "ynleborg/steam").getCurrentScore()).isEqualTo(2000);
        assertThat(find(displayableScores, "ynleborg/steam").getTournamentPoints()).isEqualTo(20);
        assertThat(find(displayableScores, "ynleborg/steam").getDelta()).isEqualTo(7280); //ratio 4.0


        assertThat(find(displayableScores, "Porkite/ps4").getInitialScore()).isEqualTo(300);
        assertThat(find(displayableScores, "Porkite/ps4").getCurrentScore()).isEqualTo(3000);
        assertThat(find(displayableScores, "Porkite/ps4").getTournamentPoints()).isEqualTo(30);
        assertThat(find(displayableScores, "Porkite/ps4").getDelta()).isEqualTo(3549L); //ratio 1.3

        assertThat(find(displayableScores, "Porkite/steam").getInitialScore()).isEqualTo(4000);
        assertThat(find(displayableScores, "Porkite/steam").getCurrentScore()).isEqualTo(4000);
        assertThat(find(displayableScores, "Porkite/steam").getTournamentPoints()).isEqualTo(0);
        assertThat(find(displayableScores, "Porkite/steam").getDelta()).isEqualTo(0);
    }

    @Test
    public void shouldReturnCombinedDisplayableScores() throws IOException {
        //given
        when(scoreRepository.getInitialScores()).thenReturn(initialScores());
        when(taClient.getScores()).thenReturn(currentScores());

        //when
        List<DisplayableScore> combinedDisplayableScores = taLeaderboardService.getCombinedDisplayableScores();

        //then
        assertThat(combinedDisplayableScores).isNotEmpty().hasSize(2);

        assertThat(find(combinedDisplayableScores, "Ynleborg").getInitialScore()).isEqualTo(300);
        assertThat(find(combinedDisplayableScores, "Ynleborg").getCurrentScore()).isEqualTo(3000);
        assertThat(find(combinedDisplayableScores, "Ynleborg").getTournamentPoints()).isEqualTo(30);
        assertThat(find(combinedDisplayableScores, "Ynleborg").getDelta()).isEqualTo(8190); //ratio 1.0 i 4.0

        assertThat(find(combinedDisplayableScores, "Porkite").getInitialScore()).isEqualTo(4300);
        assertThat(find(combinedDisplayableScores, "Porkite").getCurrentScore()).isEqualTo(7000);
        assertThat(find(combinedDisplayableScores, "Porkite").getTournamentPoints()).isEqualTo(30);
        assertThat(find(combinedDisplayableScores, "Porkite").getDelta()).isEqualTo(3549L); //ratio 1.3
    }

    private DisplayableScore find(List<DisplayableScore> scores, String key) {
        return scores.stream().filter(s -> s.getKey().equals(key)).findFirst().orElse(null);
    }

    private List<InitialScore> initialScores() {
        return Arrays.asList(
                InitialScore.builder().userName("Ynleborg").platform("xbox").score(100L).tournamentPoints(10L).build(),
                InitialScore.builder().userName("ynleborg").platform("steam").score(200L).tournamentPoints(20L).build(),
                InitialScore.builder().userName("Porkite").platform("ps4").score(300L).tournamentPoints(30L).build());
    }

    private List<InitialScore> currentScores() {
        return Arrays.asList(
                InitialScore.builder().userName("Ynleborg").platform("xbox").score(1000L).tournamentPoints(0L).build(),
                InitialScore.builder().userName("ynleborg").platform("steam").score(2000L).tournamentPoints(0L).build(),
                InitialScore.builder().userName("Porkite").platform("ps4").score(3000L).tournamentPoints(0L).build(),
                InitialScore.builder().userName("Porkite").platform("steam").score(4000L).tournamentPoints(0L).build());
    }
}
