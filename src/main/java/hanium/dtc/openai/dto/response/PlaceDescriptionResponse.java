package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PlaceDescriptionResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("place")
        String place,

        @JsonProperty("description")
        String description
) {
}
