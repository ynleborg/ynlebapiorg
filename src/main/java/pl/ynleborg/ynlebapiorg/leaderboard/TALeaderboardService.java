package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TALeaderboardService {

    private static final String CLASS = "class";

    private final ObjectMapper om = new ObjectMapper();

    public void storeCurrentScores() throws IOException {
        om.enable(SerializationFeature.INDENT_OUTPUT);
        om.writeValue(new File("store.json"), getScores());
    }

    public List<DisplayableScore> getDisplayableScores() throws IOException {
        List<Score> initialScores = om.readValue(new File("store.json"), new TypeReference<List<Score>>() {
        });

        List<Score> currentScores = getScores();
        List<DisplayableScore> result = new ArrayList<>();
        currentScores.forEach(current -> {
            Score initial = initialScores.stream().filter(is -> is.getKey().equals(current.getKey())).findFirst().orElse(current);
            result.add(DisplayableScore.builder()
                    .userName(current.getUserName())
                    .platform(current.getPlatform())
                    .icon(current.getIcon())
                    .initialScore(String.valueOf(initial.getScore()))
                    .currentScore(String.valueOf(current.getScore()))
                    .delta(String.valueOf(current.getScore() - initial.getScore()))
                    .build());
        });
        List<DisplayableScore> collect = result.stream().sorted(Comparator.comparing(DisplayableScore::getDelta).reversed()).collect(Collectors.toList());
        return collect;
    }

    public List<Score> getScores() throws IOException {

        List<Score> scores = new ArrayList<>();
        processURL(scores, "https://www.trueachievements.com/leaderboard.aspx?leaderboardid=7810");
        processURL(scores, "https://www.truetrophies.com/leaderboard.aspx?leaderboardid=393");
        processURL(scores, "https://www.truesteamachievements.com/leaderboard.aspx?leaderboardid=91");
        return scores;
    }

    private void processURL(List<Score> scores, String address) throws IOException {
        URL url = new URL(address);
        final URLConnection urlConnection = url.openConnection();
        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        urlConnection.connect();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootTagNode = htmlCleaner.clean(urlConnection.getInputStream());
        String platform = getPlatform(address);
        processLeaderboard(scores, rootTagNode, platform);
        log.info("processed: {}", address);
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

    private void processLeaderboard(List<Score> scores, TagNode rootTagNode, String platform) {
        TagNode elementByAttValue = rootTagNode.findElementByAttValue(CLASS, "maintable leaderboard", true, true);
        TagNode[] tr = elementByAttValue.getElementsByName("tr", true);
        Arrays.asList(tr).forEach(e -> {
            TagNode gamerTag = e.findElementByAttValue(CLASS, "gamer", true, true);
            if (gamerTag != null) {
                String userName = gamerTag.findElementByName("a", true).getText().toString();
                String icon = e.findElementByName("img", true).getAttributeByName("src");
                TagNode score1 = e.findElementByAttValue(CLASS, "score", true, true);
                String score = score1.getText().toString().replace(",", "");
                int parenthesisIndex = score.indexOf("(");
                if (parenthesisIndex != -1) {
                    score = score.substring(0, parenthesisIndex);
                }
                scores.add(Score.builder()
                        .key(userName + "/" + platform)
                        .userName(userName)
                        .icon("http:" + icon)
                        .score(Long.valueOf(score))
                        .platform(platform)
                        .build());
            }
        });
    }

}
