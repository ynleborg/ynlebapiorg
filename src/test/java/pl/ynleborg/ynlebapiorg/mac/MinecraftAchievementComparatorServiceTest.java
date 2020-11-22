package pl.ynleborg.ynlebapiorg.mac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MinecraftAchievementComparatorServiceTest {

    @InjectMocks
    private MinecraftAchievementComparatorService service;

    @Mock
    private XboxApiClient xboxApiClient;

    @Test
    public void should_return_all_values() {
        //given
        Achievement[] response1 = new Achievement[]{
                Achievement.builder().name("A").progressState("Achieved").rarity(rarity()).build(),
                Achievement.builder().name("B").progressState("NotAchieved").rarity(rarity()).build()
        };

        Achievement[] response2 = new Achievement[]{
                Achievement.builder().name("A").progressState("Achieved").rarity(rarity()).build(),
                Achievement.builder().name("C").progressState("NotAchieved").rarity(rarity()).build()
        };
        when(xboxApiClient.getAchievements(anyLong(), eq(1828326430L))).thenReturn(response1);
        when(xboxApiClient.getAchievements(anyLong(), eq(896928775L))).thenReturn(response2);
        when(xboxApiClient.getAchievements(anyLong(), eq(1739947436L))).thenReturn(response1);
        when(xboxApiClient.getAchievements(anyLong(), eq(1909043648L))).thenReturn(response2);
        when(xboxApiClient.getAchievements(anyLong(), eq(2047319603L))).thenReturn(response2);
        // when
        Collection<AchievementDto> model = service.getModel();

        //then
        assertThat(model).hasSize(3);
        assertThat(model.stream().filter(a -> a.getName().equals("A")).findFirst().get().getFlags()[0]).isTrue();
        assertThat(model.stream().filter(a -> a.getName().equals("A")).findFirst().get().getFlags()[1]).isTrue();
        assertThat(model.stream().filter(a -> a.getName().equals("A")).findFirst().get().getFlags()[2]).isTrue();
        assertThat(model.stream().filter(a -> a.getName().equals("A")).findFirst().get().getFlags()[3]).isTrue();
        assertThat(model.stream().filter(a -> a.getName().equals("A")).findFirst().get().getFlags()[4]).isTrue();

        assertThat(model.stream().filter(a -> a.getName().equals("B")).findFirst().get().getFlags()[0]).isFalse();
        assertThat(model.stream().filter(a -> a.getName().equals("B")).findFirst().get().getFlags()[1]).isNull();
        assertThat(model.stream().filter(a -> a.getName().equals("B")).findFirst().get().getFlags()[2]).isFalse();
        assertThat(model.stream().filter(a -> a.getName().equals("B")).findFirst().get().getFlags()[3]).isNull();
        assertThat(model.stream().filter(a -> a.getName().equals("B")).findFirst().get().getFlags()[4]).isNull();

        assertThat(model.stream().filter(a -> a.getName().equals("C")).findFirst().get().getFlags()[0]).isNull();
        assertThat(model.stream().filter(a -> a.getName().equals("C")).findFirst().get().getFlags()[1]).isFalse();
        assertThat(model.stream().filter(a -> a.getName().equals("C")).findFirst().get().getFlags()[2]).isNull();
        assertThat(model.stream().filter(a -> a.getName().equals("C")).findFirst().get().getFlags()[3]).isFalse();
        assertThat(model.stream().filter(a -> a.getName().equals("C")).findFirst().get().getFlags()[4]).isFalse();
    }

    private Rarity rarity() {
        return Rarity.builder().currentCategory("Low").currentPercentage(2.0).build();
    }

}
