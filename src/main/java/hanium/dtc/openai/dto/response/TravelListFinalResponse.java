package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelListFinalResponse(

        @JsonProperty("step")
        Integer step,

        @JsonProperty("final_message")
        String message,

        @JsonProperty("travel_id")
        Long travelId,

        @JsonProperty("final_recommends")
        List<TravelEachRecommend> travelEachRecommends
) {
}
