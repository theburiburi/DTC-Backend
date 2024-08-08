package hanium.dtc.dto.openai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record OpenAiRequest (
        @JsonProperty("model")
        String model,

        @JsonProperty("messages")
        List<OpenAiRequestMessage> messages
) {
        public static OpenAiRequest of(String model, List<String> roles, List<String> contents, String userRequest) {
                List<OpenAiRequestMessage> messages = new ArrayList<>();
                for(int index = 0; index < roles.size(); index++) {
                        messages.add(new OpenAiRequestMessage(roles.get(index), contents.get(index)));
                }
                messages.add(new OpenAiRequestMessage("user", userRequest));
                return new OpenAiRequest(model, messages);
        }
}
