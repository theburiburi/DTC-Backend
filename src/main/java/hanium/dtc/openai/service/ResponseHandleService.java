package hanium.dtc.openai.service;

import hanium.dtc.openai.dto.response.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseHandleService {
    public String[] parseFirstElement(String input) {
        return input.split("\\?", 2);
    }

    public String[] parseEachTravelPlace(String input) {
        return input.split("\\?");
    }

    public String[] parsePlaceAndDscription(String input) {
        return input.split(";");
    }

    public String ConvertOpenAiResponseToString(OpenAiResponse openAiResponse) {
        return openAiResponse.openAiChoices().get(0).openAiResponseMessage().content();
    }
}
