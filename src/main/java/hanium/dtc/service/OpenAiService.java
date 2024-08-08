package hanium.dtc.service;

import hanium.dtc.config.OpenAiPrompt;
import hanium.dtc.dto.openai.request.OpenAiRequest;
import hanium.dtc.dto.openai.response.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
