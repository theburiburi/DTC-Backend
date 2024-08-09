package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiChoice (
        @JsonProperty("message")
        OpenAiResponseMessage openAiResponseMessage,

        @JsonProperty("finish_reason")
        String finishReason
){
}
