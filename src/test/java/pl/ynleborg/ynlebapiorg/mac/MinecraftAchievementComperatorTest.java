package pl.ynleborg.ynlebapiorg.mac;

import org.junit.Test;
import pl.ynleborg.ynlebapiorg.mac.MinecraftAchievementComperatorService;

import static org.assertj.core.api.Assertions.assertThat;

public class MinecraftAchievementComperatorTest {

    @Test
    public void shouldExtractRatio() {
        //given
        String chartList = "Unlocked by 18,589 tracked gamers (37% - TA Ratio = 1.63)\n&nbsp;\n";

        //when
        Double ratio = new MinecraftAchievementComperatorService().extractRatio(chartList);

        //then
        assertThat(ratio).isEqualTo(1.63);
    }
}