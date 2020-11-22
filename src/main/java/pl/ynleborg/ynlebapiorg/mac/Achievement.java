package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {

    private long id;
    private String name;
    private String description;
    private String progressState;
    private Rarity rarity;
    private Progression progression;
    private List<String> platforms;
}
