package hanium.dtc.openai.service;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.openai.dto.response.PlaceDescriptionResponse;
import hanium.dtc.openai.dto.response.TravelListResponse;
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
    public void setUserState(Long userId, String userRequest, Integer userStep) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        user.setTendency(userRequest);
        user.setQuestionStep(userStep);
    }

    @Transactional
    public TravelListResponse getListOfTravel(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String[] gptResponseList =  responseHandleService.parseFirstElement(getTravelRecommendation(userRequest));
        String placeDescription =  gptResponseList[0];
        String placeList = gptResponseList[1];
        String placeListWithDescription = responseHandleService.ConvertOpenAiResponseToString(getTravelDescription(placeList));
        String[] listOfEachPlace = responseHandleService.parseEachTravelPlace(placeListWithDescription);

        user.setTendency(userRequest);
        for(String eachPlace : listOfEachPlace) {
            try {
                String[] eachPlaceAndDescription = responseHandleService.parsePlaceAndDscription(eachPlace);
                createTemporaryTravel(1L, eachPlaceAndDescription);
            } catch(Exception e) {
                log.info("에러 발생함");
            }
        }
        // setUserState(userId, userRequest, 2);

        return TravelListResponse.builder()
                .description(placeDescription)
                .placeDescriptionResponses(user.getTemporaryTravels().stream()
                        .map(temporaryTravel ->
                                PlaceDescriptionResponse.builder()
                                        .placeId(temporaryTravel.getId())
                                        .place(temporaryTravel.getPlace())
                                        .description(temporaryTravel.getDescription())
                                        .build())
                        .toList())
                .build();
    }

    @Transactional
    public String createTravelTimeTable(Long userId, String userRequest) {
        return null;
    }

    public Object getOpenAiResponse(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        switch (user.getQuestionStep()) {
            case 1: // 첫 질문에 대한 유저의 응답
                return getListOfTravel(userId, userRequest);
            case 2: // 다음 스탭
                return createTravelTimeTable(userId, userRequest);
            default:
                break;
        }

        return new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
