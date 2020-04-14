package pl.ynleborg.ynlebapiorg.mac;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class MinecraftAchievementComperatorService {

    private static final String CLASS = "class";
    private static final Pattern CHART_PATTERN = Pattern.compile("(\\d*\\.\\d*)");
    private static final List<String> urls = Arrays.asList(
            "https://www.trueachievements.com/game/Minecraft/achievements?gamerid=258489",
            "https://www.trueachievements.com/game/Minecraft-Windows-10/achievements?gamerid=258489",
            "https://www.trueachievements.com/game/Minecraft-Pocket-Edition/achievements?gamerid=258489",
            "https://www.trueachievements.com/game/Minecraft-Pocket-Edition-Gear-VR/achievements?gamerid=258489",
            "https://www.trueachievements.com/game/Minecraft-Nintendo-Switch/achievements?gamerid=258489"

    );

    public Collection<Achievement> getModel() throws IOException {
        Map<String, Achievement> data = new TreeMap<>();
        for (int i = 0; i < urls.size(); i++) {
            processURL(data, urls.get(i), i);
        }
        return data.values();
    }

    private void processURL(Map<String, Achievement> data, String address, int index) throws IOException {
        URL url = new URL(address);
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootTagNode = htmlCleaner.clean(url);
        processPanels(data, rootTagNode, "w", index);
        processPanels(data, rootTagNode, "nw", index);
    }

    private void processPanels(Map<String, Achievement> data, TagNode rootTagNode, String className, int index) {
        TagNode[] elementByAttValue = rootTagNode.getElementsByAttValue(CLASS, className, true, true);
        Arrays.asList(elementByAttValue).forEach(e -> {
            TagNode mainlink = e.findElementByAttValue(CLASS, "title", true, true);
            String name = decrapify(mainlink.getText().toString().trim());
            log.info("name:{}", name);
            String href = mainlink.getAttributeByName("href");
            String description = e.getChildTagList().get(1).getText().toString();
            TagNode elementByAttValue1 = e.getChildTagList().get(3);
            Double ratio = extractRatio(elementByAttValue1.getAttributeByName("data-af"));

            Achievement candidate = data.get(name);
            boolean unlocked = className.equals("w");
            if (candidate != null) {
                candidate.getFlags()[index] = unlocked;
            } else {
                Achievement achievement = Achievement.builder()
                        .name(name)
                        .href("https://www.trueachievements.com" + href)
                        .description(description)
                        .ratio(ratio)
                        .flags(new Boolean[urls.size()])
                        .build();
                achievement.getFlags()[index] = unlocked;
                data.put(name, achievement);
            }
        });
    }

    public Double extractRatio(String text) {
        double ratio = 0.0;
        Matcher marcher = CHART_PATTERN.matcher(text);
        while (marcher.find()) {
            ratio = Double.parseDouble(marcher.group(1));
        }
        return ratio;

    }

    private String decrapify(String name) {
        if ("Into The Nether".equals(name)) {
            return "Into the Nether";
        } else if ("Tie Die Outfit".equals(name)) {
            return "Tie Dye Outfit";
        } else if ("Sail the seven seas".equals(name)) {
            return "Sail the 7 Seas";
        } else if ("On a Rail".equals(name)) {
            return "On A Rail";
        } else if ("Tie-Dye Outfit".equals(name)) {
            return "Tie Dye Outfit";
        } else if ("Lion Tamer".equals(name)) {
            return "Lion Hunter";
        } else if ("So, I've Got That Going for Me".equals(name) || "So I've Got That Going for Me".equals(name)) {
            return "So I Got That Going for Me";
        } else if ("Organisational Wizard".equals(name)) {
            return "Organizational Wizard";
        } else if ("I am a Marine Biologist".equals(name)) {
            return "I am a marine biologist";
        } else if ("Sleep with the Fishes".equals(name)) {
            return "Sleep with the fishes";
        } else if ("Dispense with This".equals(name)) {
            return "Dispense With This";
        } else {
            return name;
        }
    }
}
