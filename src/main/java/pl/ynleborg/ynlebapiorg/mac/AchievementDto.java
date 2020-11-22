package pl.ynleborg.ynlebapiorg.mac;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AchievementDto {
    private String name;
    private String href;
    private String description;
    private Double ratio;
    private List<Platform> platforms;
}
