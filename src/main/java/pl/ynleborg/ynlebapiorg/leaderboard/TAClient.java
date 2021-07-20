package pl.ynleborg.ynlebapiorg.leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TAClient {

    private static final String CLASS = "class";

    public List<InitialScore> getScores() throws IOException {
        log.info("Loading data from true*achievements");
        List<InitialScore> initialScores = new ArrayList<>();
        processURL(initialScores, "https://www.trueachievements.com/leaderboard.aspx?leaderboardid=7810");
        processURL(initialScores, "https://www.truetrophies.com/leaderboard.aspx?leaderboardid=393");
        processURL(initialScores, "https://www.truesteamachievements.com/leaderboard.aspx?leaderboardid=91");
        return initialScores;
    }

    private void processURL(List<InitialScore> initialScores, String address) throws IOException {
        URL url = new URL(address);
        final URLConnection urlConnection = url.openConnection();
        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        urlConnection.connect();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootTagNode = htmlCleaner.clean(urlConnection.getInputStream());
        String platform = getPlatform(address);
        processLeaderboard(initialScores, rootTagNode, platform);
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

    private void processLeaderboard(List<InitialScore> initialScores, TagNode rootTagNode, String platform) {
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
                initialScores.add(InitialScore.builder()
                        .userName(userName)
                        .icon(handle(icon, userName, platform))
                        .score(Long.valueOf(score))
                        .tournamentPoints(0L)
                        .platform(platform)
                        .build());
            }
        });
    }

    private String handle(String icon, String username, String platform) {
        String imagePath = "avatars/" + username + "_" + platform + icon.substring(icon.length() - 4);
        try {
            String iconUri = "http:" + icon;
            URL website = new URL(iconUri);
            //ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            //FileOutputStream fos = new FileOutputStream(localPath);
            //fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            HttpURLConnection httpcon = (HttpURLConnection) website.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            try (InputStream in = httpcon.getInputStream()) {
                Path p = Paths.get(imagePath);
                if (!Files.exists(p)) {
                    Files.createDirectories(p);
                }
                Files.copy(in, Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return imagePath;
    }
}
