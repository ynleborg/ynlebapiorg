package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class MinecraftAchievementComparatorService {

    //                                     xbox;        win10;      android;     gearvr;      switch;      ios
    private static final long[] gameIds = {1828326430L, 896928775L, 1739947436L, 1909043648L, 2047319603L, 1810924247L};
    private static final long XUID = 2533274848778089L; //ynleborg
    private final XboxApiClient xboxApiClient;


    public Collection<AchievementDto> getModel() {
        Map<String, AchievementDto> result = new HashMap<>();
        Arrays.stream(gameIds).sequential().forEach(gameId -> Arrays.asList(xboxApiClient.getAchievements(XUID, gameId)).
                forEach(a -> result
                        .computeIfAbsent(
                                getSanitizedName(a.getName()),
                                sanitizedName -> AchievementDto.builder()
                                        .name(sanitizedName)
                                        .description(a.getDescription())
                                        .platforms(new ArrayList<>())
                                        .ratio(a.getRarity().getCurrentPercentage())
                                        .build())
                        .getPlatforms().addAll(isAchieved(a) ? Collections.singleton(Platform.valueOfLabel(a.getPlatforms().get(0))) : Collections.emptyList())));
        return result.values();
    }

    private boolean isAchieved(Achievement a) {
        if (a.getProgression() != null
                && !a.getProgression().getRequirements().isEmpty()
                && a.getProgression().getRequirements().get(0).getCurrent() != null
                && !a.getProgressState().equals("Achieved")) {
            log.info(a.getPlatforms().get(0) + " " + a.getName() + " " + a.getProgression().getRequirements().get(0).toString());
        }
        return a.getProgressState().equals("Achieved");
    }

    private String getSanitizedName(String name) {
        if ("Dispense with This".equals(name)) {
            return "Dispense With This";
        }
        if ("Into the Nether".equals(name)) {
            return "Into The Nether";
        }
        if ("So I've Got That Going for Me".equals(name)) {
            return "So I Got That Going for Me";
        }
        if ("Organisational Wizard".equals(name)) {
            return "Organizational Wizard";
        }
        if ("Sleep with the Fishes".equals(name)) {
            return "Sleep with the fishes";
        }
        if ("Sail the 7 Seas".equals(name)) {
            return "Sail the seven seas";
        }
        return name;
    }
}
