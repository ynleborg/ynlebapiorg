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

    public Collection<Achievement> getModel() throws IOException {
        Map<String, Achievement> data = new TreeMap<>();
        processURL(data, "https://www.trueachievements.com/game/Minecraft/achievements?gamerid=258489", 0);
        processURL(data, "https://www.trueachievements.com/game/Minecraft-Windows-10/achievements?gamerid=258489", 1);
        processURL(data, "https://www.trueachievements.com/game/Minecraft-Pocket-Edition/achievements?gamerid=258489", 2);
        processURL(data, "https://www.trueachievements.com/game/Minecraft-Pocket-Edition-Gear-VR/achievements?gamerid=258489", 3);
        return data.values();
    }

    private void processURL(Map<String, Achievement> data, String address, int index) throws IOException {
        URL url = new URL(address);
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootTagNode = htmlCleaner.clean(url);
        processPanels(data, rootTagNode, "maincolumnpanel achievementpanel green", index);
        processPanels(data, rootTagNode, "maincolumnpanel achievementpanel red", index);
    }

    private void processPanels(Map<String, Achievement> data, TagNode rootTagNode, String className, int index) {
        TagNode[] elementByAttValue = rootTagNode.getElementsByAttValue(CLASS, className, true, true);
        Arrays.asList(elementByAttValue).forEach(e -> {
            String name = decrapify(e.findElementByAttValue(CLASS, "mainlink", true, true).getText().toString().trim());
            String description = e.findElementByAttValue(CLASS, "subheader", true, true).getText().toString().trim();
            Double ratio = extractRatio(e.findElementByAttValue(CLASS, "chartlist", true, true).getText().toString());

            Achievement candidate = data.get(name);
            Boolean unlocked = className.endsWith("green");
            if (candidate != null) {
                candidate.getFlags()[index] = unlocked;
            } else {
                Achievement achievement = Achievement.builder().name(name).description(description).ratio(ratio).flags(new Boolean[4]).build();
                achievement.getFlags()[index] = unlocked;
                data.put(name, achievement);
            }
        });
    }

    public Double extractRatio(String text) {
        Double ratio = 0.0;
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
        } else {
            return name;
        }
    }
}
