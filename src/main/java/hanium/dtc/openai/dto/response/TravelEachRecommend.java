package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TravelEachRecommend(
        @JsonProperty("id")
        Long id,

        @JsonProperty("day")
        String day,

        @JsonProperty("plan")
        String plan
) {
}
