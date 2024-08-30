package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelListResponse(
        @JsonProperty("step")
        Integer step,

        @JsonProperty("description")
        String description,

        @JsonProperty("places")
        List<PlaceDescriptionResponse> placeDescriptionResponses
) {
}
