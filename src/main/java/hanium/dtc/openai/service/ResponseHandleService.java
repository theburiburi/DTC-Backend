package hanium.dtc.openai.service;

import hanium.dtc.openai.dto.response.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<String> parseTimeTable(String input) {
        return new ArrayList<>(Arrays.asList(input.split("}")));
    }

    public List<String> parseTimeAndTravel(String input) {
        return new ArrayList<>(Arrays.asList(input.split("&")));
    }

    public List<String> parseEachTimeOfDay(String input) {
        return new ArrayList<>(Arrays.asList(input.split("\n")));
    }

    public List<String> parseTimeAndSchedule(String input) {
        return new ArrayList<>(Arrays.asList(input.split("-")));
    }

    public String convertOpenAiResponseToString(OpenAiResponse openAiResponse) {
        return openAiResponse.openAiChoices().get(0).openAiResponseMessage().content();
    }

    public List<Long> parsePreferPlace(String input) {
        return Arrays.stream(input.split(" ")).map(
                placeId -> Long.parseLong(placeId)
        ).toList();
    }

    public String dateToDateString(Integer date) {
        String dateString = "9.여행 일정 : ";

        switch (date) {
            case 1:
                dateString += "당일치기, ";
                break;
            default:
                dateString += Integer.toString(date - 1) + "박" + Integer.toString(date) + "일, ";
                break;
        }

        return dateString;
    }
}
