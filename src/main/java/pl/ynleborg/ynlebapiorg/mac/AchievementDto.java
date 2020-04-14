package pl.ynleborg.ynlebapiorg.mac;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementDto {
    private String name;
    private String href;
    private String description;
    private Double ratio;
    private Boolean[] flags;
}
