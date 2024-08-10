package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PlaceDescriptionResponse(
        @JsonProperty("place_id")
        Long placeId,

        @JsonProperty("place")
        String place,

        @JsonProperty("description")
        String description
) {
}
