package hanium.dtc.dto.openai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record OpenAiRequestMessage(
        @JsonProperty("role")
        String role,

        @JsonProperty("content")
        String content
) {

}
