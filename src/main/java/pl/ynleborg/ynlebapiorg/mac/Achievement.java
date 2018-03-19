package pl.ynleborg.ynlebapiorg.mac;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Achievement {
    private String name;
    private String description;
    private Double ratio;
    private Boolean[] flags;
}
