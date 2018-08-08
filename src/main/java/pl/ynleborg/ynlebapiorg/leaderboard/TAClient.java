package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TAClient {

    private static final String CLASS = "class";

    @Cacheable(value = "test", cacheManager = "myCacheManager")
    public List<Score> getScores() throws IOException {
        log.info("Loading data from true*achievements");
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
                TagNode scoreTag = e.findElementByAttValue(CLASS, "score", true, true);
                String score = scoreTag.getText().toString().replace(",", "").replace(".00", "");
                int parenthesisIndex = score.indexOf("(");
                if (parenthesisIndex != -1) {
                    score = score.substring(0, parenthesisIndex);
                }
                scores.add(Score.builder()
                        .key(userName + "/" + platform)
                        .userName(userName)
                        .icon("http:" + icon)
                        .score(Long.valueOf(score))
                        .tournamentPoints(0L)
                        .platform(platform)
                        .build());
            }
        });
    }
}
