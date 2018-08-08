package pl.ynleborg.ynlebapiorg.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"key"})
public class InitialScore {

    private String userName;

    private String platform;

    private String icon;

    private Long tournamentPoints;

    private Long score;

    public String getKey() {
        return userName + '/' + platform;
    }

}
