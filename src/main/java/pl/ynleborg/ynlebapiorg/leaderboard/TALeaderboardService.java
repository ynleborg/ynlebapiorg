package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.stereotype.Service;
import pl.ynleborg.ynlebapiorg.mac.Achievement;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TALeaderboardService {

    private static final String CLASS = "class";
    private static final Pattern CHART_PATTERN = Pattern.compile("(\\d*\\.\\d*)");

    public Collection<Score> getModel() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        List<Score> scores = new ArrayList<>();
        processURL(scores, date, "https://www.trueachievements.com/leaderboard.aspx?leaderboardid=7810");
        processURL(scores, date, "https://www.truetrophies.com/leaderboard.aspx?leaderboardid=393");
        //processURL(scores, "https://truesteamachievements.com/leaderboard.aspx?leaderboardid=91");
        return scores;
    }

    private void processURL(List<Score> scores, String date, String address) throws IOException {
        URL url = new URL(address);
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootTagNode = htmlCleaner.clean(url);
        String platform = getPlatform(address);
        processLeaderboard(scores, rootTagNode, date, platform);
    }

    private String getPlatform(String address) {
        if (address.contains("trueachievements")) {
            return "xbox";
        } else if (address.contains("truetrophies")) {
            return "ps4";
        } else {
            return "steam";
        }
    }

    private void processLeaderboard(List<Score> scores, TagNode rootTagNode, String date, String platform) {
        TagNode elementByAttValue = rootTagNode.findElementByAttValue(CLASS, "maintable leaderboard", true, true);
        TagNode[] tr = elementByAttValue.getElementsByName("tr", true);
        Arrays.asList(tr).forEach(e -> {
            TagNode gamerTag = e.findElementByAttValue(CLASS, "gamer", true, true);
            if (gamerTag != null) {
                String userName = gamerTag.findElementByName("a", true).getText().toString();
                String icon = e.findElementByName("img", true).getAttributeByName("src");
                TagNode score1 = e.findElementByAttValue(CLASS, "score", true, true);
                String score = score1.getText().toString().substring(0, score1.getText().toString().indexOf("("));
                scores.add(Score.builder()
                        .userName(userName)
                        .icon("http:" + icon)
                        .score(score)
                        .platform(platform)
                        .date(date)
                        .build());
            }
        });
    }

}