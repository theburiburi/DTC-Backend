package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelRecommendResponse(
        @JsonProperty("recommends")
        List<TravelEachRecommend> travelEachRecommends
) {

}
