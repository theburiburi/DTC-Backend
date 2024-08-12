package hanium.dtc.openai.service;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.openai.dto.response.PlaceDescriptionResponse;
import hanium.dtc.openai.dto.response.TravelListResponse;
import hanium.dtc.openai.prompt.TravelRecommendationPrompt;
import hanium.dtc.openai.prompt.TravelDescriptionPrompt;
import hanium.dtc.openai.dto.request.OpenAiRequest;
import hanium.dtc.openai.dto.response.OpenAiResponse;
import hanium.dtc.openai.prompt.TravelTendencyPrompt;
import hanium.dtc.openai.prompt.TravelTimetablePrompt;
import hanium.dtc.travel.domain.TemporaryTravel;
import hanium.dtc.travel.repository.TemporaryPlaceRepository;
import hanium.dtc.travel.repository.TemporaryTravelRepository;
import hanium.dtc.travel.service.TemporaryTravelService;
import hanium.dtc.user.domain.User;
import hanium.dtc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.model}")
    private String openAiModel;
    private final RestClient openAiRestClient;
    private final TravelRecommendationPrompt travelRecommendationPrompt;
    private final TravelDescriptionPrompt travelDescriptionPrompt;
    private final TravelTendencyPrompt travelTendencyPrompt;
    private final TravelTimetablePrompt travelTimetablePrompt;
    private final ResponseHandleService responseHandleService;
    private final TemporaryTravelService temporaryTravelService;
    private final UserRepository userRepository;
    private final TemporaryTravelRepository temporaryTravelRepository;
    private final TemporaryPlaceRepository temporaryPlaceRepository;

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

    public OpenAiResponse getTravelTendency(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelTendencyPrompt.getRoles(), travelTendencyPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelTimeTable(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelTimetablePrompt.getRoles(), travelTimetablePrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    @Transactional
    public void setTravelState(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        temporaryTravel.updateTendency(userRequest);
        temporaryTravel.nextStep();
    }

    @Transactional
    public TravelListResponse getListOfTravel(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String userTendency = responseHandleService.ConvertOpenAiResponseToString(getTravelTendency(userRequest));
        String[] gptResponseList =  responseHandleService.parseFirstElement(getTravelRecommendation(userRequest));
        String placeDescription =  gptResponseList[0];
        String placeList = gptResponseList[1];
        String placeListWithDescription = responseHandleService.ConvertOpenAiResponseToString(getTravelDescription(placeList));
        String[] listOfEachPlace = responseHandleService.parseEachTravelPlace(placeListWithDescription);

        setTravelState(userId, userTendency);
        for(String eachPlace : listOfEachPlace) {
            try {
                String[] eachPlaceAndDescription = responseHandleService.parsePlaceAndDscription(eachPlace);
                temporaryTravelService.createTemporaryPlace(userId, eachPlaceAndDescription);
            } catch (Exception e) {
                throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        return TravelListResponse.builder()
                .description(placeDescription)
                .placeDescriptionResponses(user.getTemporaryTravel().getTemporaryPlaces().stream()
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
    public OpenAiResponse createTravelTimeTable(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);
        List<Long> preferPlaces = responseHandleService.parsePreferPlace(userRequest);
        Long day = ChronoUnit.DAYS.between(temporaryTravel.getDepartAt(), temporaryTravel.getArriveAt());
        Integer dayBetween = day.intValue();

        String placeString = "8.선호하는 여행지 목록 : ";
        for(Long preferplace : preferPlaces) {
            placeString += temporaryPlaceRepository.findById(preferplace)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TEMP_PLACE))
                    .getPlace() + ", ";
        }
        placeString += responseHandleService.dateToDateString(dayBetween); // 여행 날짜 추가
        placeString += "10.인원 : " + temporaryTravel.getPerson().toString();
        placeString = temporaryTravel.getTendency() + placeString;

        return getTravelTimeTable(placeString);
    }

    @Transactional
    public Object getOpenAiResponse(Long userId, String userRequest) throws RuntimeException{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        switch (temporaryTravelRepository.findByUser(user).getQuestionStep()) {
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
