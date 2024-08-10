package hanium.dtc.openai.service;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.openai.prompt.TravelRecommendationPrompt;
import hanium.dtc.openai.prompt.TravelDescriptionPrompt;
import hanium.dtc.openai.dto.request.OpenAiRequest;
import hanium.dtc.openai.dto.response.OpenAiResponse;
import hanium.dtc.travel.domain.TemporaryTravel;
import hanium.dtc.travel.repository.TemporaryTravelRepository;
import hanium.dtc.user.domain.User;
import hanium.dtc.user.repository.UserRepository;
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
    private final TravelRecommendationPrompt travelRecommendationPrompt;
    private final TravelDescriptionPrompt travelDescriptionPrompt;
    private final ResponseHandleService responseHandleService;
    private final UserRepository userRepository;
    private final TemporaryTravelRepository temporaryTravelRepository;

    public String getTravelRecommendation(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelRecommendationPrompt.getRoles(), travelRecommendationPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody()
                .openAiChoices().get(0)
                .openAiResponseMessage().content();
    }

    public OpenAiResponse getTravelDescription(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelDescriptionPrompt.getRoles(), travelDescriptionPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    @Transactional
    public void createTemporaryTravel(Long userId, String[] eachPlaceAndDescription) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        temporaryTravelRepository.save(TemporaryTravel.builder()
                        .place(eachPlaceAndDescription[0])
                        .description(eachPlaceAndDescription[1])
                        .user(user)
                .build());
    }

    @Transactional
    public String getListOfTravel(String userRequest) {
        String[] gptResponseList =  responseHandleService.parseFirstElement(getTravelRecommendation(userRequest));

        String placeDescription =  gptResponseList[0];
        log.info("1차 검증 지점 " + placeDescription);
        String placeList = gptResponseList[1];
        log.info("2차 검증 지점 " + placeList);

        String placeListWithDescription = responseHandleService.ConvertOpenAiResponseToString(getTravelDescription(placeList));
        String[] listOfEachPlace = responseHandleService.parseEachTravelPlace(placeListWithDescription);

        for(String eachPlace : listOfEachPlace) {
            try {
                String[] eachPlaceAndDescription = responseHandleService.parsePlaceAndDscription(eachPlace);
                createTemporaryTravel(1L, eachPlaceAndDescription);
            } catch(Exception e) {
                log.info("에러 발생함");
            }
        }

        return placeDescription + " " + placeListWithDescription;
    }
}
