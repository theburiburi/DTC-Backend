package hanium.dtc.openai.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiResponse (
        @JsonProperty("model")
        String model,

        @JsonProperty("choices")
        List<OpenAiChoice> openAiChoices
) {
}
