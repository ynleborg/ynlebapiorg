package pl.ynleborg.ynlebapiorg.mac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rarity {
    private String currentCategory;
    private Double currentPercentage;
}
