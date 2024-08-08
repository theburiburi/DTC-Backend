package hanium.dtc.dto.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenAiResponseMessage(
        @JsonProperty("role")
        String role,

        @JsonProperty("content")
        String content
) {
}
