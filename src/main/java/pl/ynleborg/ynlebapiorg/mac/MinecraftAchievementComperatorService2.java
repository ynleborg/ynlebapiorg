package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@AllArgsConstructor
public class MinecraftAchievementComperatorService2 {

    private static long[] gameIds = {1828326430L, 896928775L, 1739947436L, 1909043648L, 2047319603L};
    private XboxApiClient xboxApiClient;

    //xbox;win10;android;gearvr;switch
    public Collection<AchievementDto> getModel() {
        Map<String, AchievementDto> result = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            final int index = i;
            long gameId = gameIds[i];
            log.info("index={}, gameId={}", index, gameId);
            Arrays.asList(xboxApiClient.getAchievements(2533274848778089L, gameId).block()).
                    forEach(a -> handle(index, getSanitazedName(a.getName()), a, result));
        }
        return result.values();
    }

    private void handle(int i, String sanitazedName, Achievement a, Map<String, AchievementDto> result) {
        if (result.get(sanitazedName) != null) {
            AchievementDto achievementDto = result.get(sanitazedName);
            achievementDto.getFlags()[i] = a.getProgressState().equals("Achieved");
        } else {
            AchievementDto achievementDto = AchievementDto.builder()
                    .name(sanitazedName)
                    .description(a.getDescription())
                    .flags(new Boolean[5])
                    .ratio(a.getRarity().getCurrentPercentage())
                    .build();
            achievementDto.getFlags()[i] = a.getProgressState().equals("Achieved");
            result.put(sanitazedName, achievementDto);
        }
    }

    private String getSanitazedName(String name) {
        if ("Dispense with This" .equals(name)) {
            return "Dispense With This";
        }
        if ("Into the Nether" .equals(name)) {
            return "Into The Nether";
        }
        if ("So I've Got That Going for Me" .equals(name)) {
            return "So I Got That Going for Me";
        }
        if ("Organisational Wizard" .equals(name)) {
            return "Organizational Wizard";
        }
        if ("Sleep with the Fishes" .equals(name)) {
            return "Sleep with the fishes";
        }
        if ("Sail the 7 Seas" .equals(name)) {
            return "Sail the seven seas";
        }
        return name;
    }
}
