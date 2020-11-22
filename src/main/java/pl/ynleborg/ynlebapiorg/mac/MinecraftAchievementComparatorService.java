package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@Slf4j
@AllArgsConstructor
public class MinecraftAchievementComparatorService {

    private static final long[] gameIds = {1828326430L, 896928775L, 1739947436L, 1909043648L, 2047319603L};
    private static final long XUID = 2533274848778089L;
    private final XboxApiClient xboxApiClient;

    //xbox;win10;android;gearvr;switch
    public Collection<AchievementDto> getModel() {
        Map<String, AchievementDto> result = new HashMap<>();
        IntStream.range(0, 5).forEach(index -> Arrays.asList(xboxApiClient.getAchievements(XUID, gameIds[index])).
                forEach(a -> result.computeIfAbsent(
                        getSanitizedName(a.getName()),
                        sanitizedName -> AchievementDto.builder()
                                .name(sanitizedName)
                                .description(a.getDescription())
                                .flags(new Boolean[5])
                                .ratio(a.getRarity().getCurrentPercentage())
                                .build())
                        .getFlags()[index] = isAchieved(a)));
        return result.values();
    }

    private boolean isAchieved(Achievement a) {
        if (a.getProgression() != null && a.getProgression().getRequirements().size() > 0 && a.getProgression().getRequirements().get(0).getCurrent() != null && !a.getProgressState().equals("Achieved")) {
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
