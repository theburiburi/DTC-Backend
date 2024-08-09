package hanium.dtc.openai.service;

import hanium.dtc.config.OpenAiPrompt;
import hanium.dtc.openai.dto.request.OpenAiRequest;
import hanium.dtc.openai.dto.response.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.model}")
    private String openAiModel;
    private final RestClient openAiRestClient;
    private final OpenAiPrompt openAiPrompt;

    public OpenAiResponse getResponseOfQuestion(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, openAiPrompt.getRoles(), openAiPrompt.getContents(), userRequest))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }
}
