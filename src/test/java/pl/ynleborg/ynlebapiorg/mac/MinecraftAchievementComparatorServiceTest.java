package pl.ynleborg.ynlebapiorg.mac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.ynleborg.ynlebapiorg.mac.Platform.*;

@RunWith(MockitoJUnitRunner.class)
public class MinecraftAchievementComparatorServiceTest {

    @InjectMocks
    private MinecraftAchievementComparatorService service;

    @Mock
    private XboxApiClient xboxApiClient;

    @Test
    public void should_return_all_values() {
        //given
        when(xboxApiClient.getAchievements(anyLong(), eq(1828326430L))).thenReturn(createResponse("B", X));
        when(xboxApiClient.getAchievements(anyLong(), eq(896928775L))).thenReturn(createResponse("C", W));
        when(xboxApiClient.getAchievements(anyLong(), eq(1739947436L))).thenReturn(createResponse("B", A));
        when(xboxApiClient.getAchievements(anyLong(), eq(1909043648L))).thenReturn(createResponse("C", G));
        when(xboxApiClient.getAchievements(anyLong(), eq(2047319603L))).thenReturn(createResponse("C", N));
        // when
        List<AchievementDto> model = new ArrayList<>(service.getModel("ynleborg", new Platform[]{X, W, A, G, N}));

        //then
        assertThat(model).hasSize(3);

        for (AchievementDto achievementDto : model) {
            if (achievementDto.getName().equals("A")) {
                assertThat(achievementDto.getPlatforms()).containsAll(Arrays.asList(X, W, A, G, N));
            }
            if (achievementDto.getName().equals("B")) {
                assertThat(achievementDto.getPlatforms()).containsAll(Collections.emptyList());
            }
            if (achievementDto.getName().equals("C")) {
                assertThat(achievementDto.getPlatforms()).containsAll(Collections.emptyList());
            }
        }

    }

    private Achievement[] createResponse(String b, Platform platform) {
        return new Achievement[]{
                Achievement.builder().name("A").progressState("Achieved").rarity(rarity()).platforms(Collections.singletonList(platform.label)).build(),
                Achievement.builder().name(b).progressState("NotAchieved").rarity(rarity()).platforms(Collections.singletonList(platform.label)).build()
        };
    }

    private Rarity rarity() {
        return Rarity.builder().currentCategory("Low").currentPercentage(2.0).build();
    }

}
