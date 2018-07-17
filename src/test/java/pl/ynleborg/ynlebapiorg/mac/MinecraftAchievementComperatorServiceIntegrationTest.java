package pl.ynleborg.ynlebapiorg.mac;

import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class MinecraftAchievementComperatorServiceIntegrationTest {

    private MinecraftAchievementComperatorService service = new MinecraftAchievementComperatorService();

    @Test
    public void shouldCreateModel() throws IOException {
        //when
        Collection<Achievement> model = service.getModel();

        //then
        assertThat(model).isNotEmpty();
        assertThat(model.size()).isEqualTo(91);
        assertThat(model.stream().filter(n -> n.getName().equals("Acquire Hardware")).findFirst().get().getFlags()).doesNotContain(Boolean.FALSE);

    }
}