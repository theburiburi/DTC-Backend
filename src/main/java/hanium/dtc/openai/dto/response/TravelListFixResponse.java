package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelListFixResponse(
        @JsonProperty("step")
        Integer step,

        @JsonProperty("message")
        String message,

        @JsonProperty("recommends")
        List<TravelEachRecommend> travelEachRecommends
) {
}
