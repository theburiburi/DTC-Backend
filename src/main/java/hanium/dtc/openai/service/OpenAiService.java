package hanium.dtc.openai.service;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.openai.dto.response.*;
import hanium.dtc.openai.prompt.*;
import hanium.dtc.openai.dto.request.OpenAiRequest;
import hanium.dtc.travel.domain.RecordDetail;
import hanium.dtc.travel.domain.TemporaryRecommend;
import hanium.dtc.travel.domain.TemporaryTravel;
import hanium.dtc.travel.domain.TravelRecord;
import hanium.dtc.travel.repository.*;
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
import java.util.Arrays;
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
    private final SelectNextStepPrompt selectNextStepPrompt;
    private final TravelPlanInfoPrompt travelPlanInfoPrompt;
    private final ResponseHandleService responseHandleService;
    private final TemporaryTravelService temporaryTravelService;
    private final UserRepository userRepository;
    private final TravelRecordRepository travelRecordRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final TemporaryTravelRepository temporaryTravelRepository;
    private final TemporaryPlaceRepository temporaryPlaceRepository;
    private final TemporaryRecommendRepository temporaryRecommendRepository;

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

    public OpenAiResponse getNextStep(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, selectNextStepPrompt.getRoles(), selectNextStepPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelPlanInfo(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelPlanInfoPrompt.getRoles(), travelPlanInfoPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    @Transactional
    public void setTravelState(Long userId, String place, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        temporaryTravel.updatePlace(place);
        temporaryTravel.updateTendency(userRequest);
        temporaryTravel.nextStep();
    }

    @Transactional
    public void saveTravelRecord(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        // 여행 계획이 확정이니까 임시 여행기록에 있던 데이터들을 여행기록으로 전환
        TravelRecord travelRecord = new TravelRecord(temporaryTravel);
        travelRecordRepository.save(travelRecord);

        List<String> recommends = temporaryRecommendRepository.findAllRecommendByTemporaryTravel(temporaryTravel);
        List<String> subRecommends = recommends.subList(0, recommends.size()-1);

        int i = 1;
        for(String recommend : subRecommends) {
            List<String> RecordDetailOfDays = responseHandleService.parseEachTimeOfDay(recommend);
            for(String eachRecordDetail : RecordDetailOfDays) {
                String time = responseHandleService.parseTimeAndSchedule(eachRecordDetail).get(0).trim();
                String planOfTime = responseHandleService.parseTimeAndSchedule(eachRecordDetail).get(1).trim();
                log.info(responseHandleService.convertOpenAiResponseToString(getTravelPlanInfo(planOfTime + " & " + travelRecord.getPlace())));
            }
            i++;
        }


    }

    @Transactional
    public TravelListResponse getListOfTravel(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String userTendency = responseHandleService.convertOpenAiResponseToString(getTravelTendency(userRequest));
        String[] gptResponseList =  responseHandleService.parseFirstElement(getTravelRecommendation(userRequest));
        String place = gptResponseList[0];
        String[] getDescriptionAndPlace = responseHandleService.parseFirstElement(gptResponseList[1]);
        String placeDescription =  getDescriptionAndPlace[0];
        String placeList = getDescriptionAndPlace[1];
        String placeListWithDescription = responseHandleService.convertOpenAiResponseToString(getTravelDescription(placeList));
        String[] listOfEachPlace = responseHandleService.parseEachTravelPlace(placeListWithDescription);

        setTravelState(userId, place, userTendency);
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
    public TravelRecommendResponse createTravelTimeTable(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);
        List<Long> preferPlaces = responseHandleService.parsePreferPlace(userRequest);
        Long day = ChronoUnit.DAYS.between(temporaryTravel.getDepartAt(), temporaryTravel.getArriveAt());
        Integer dayBetween = day.intValue() + 1;

        String placeString = "8.선호하는 여행지 목록 : ";
        for(Long preferplace : preferPlaces) {
            placeString += temporaryPlaceRepository.findById(preferplace)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TEMP_PLACE))
                    .getPlace() + ", ";
        }
        placeString += responseHandleService.dateToDateString(dayBetween); // 여행 날짜 추가
        placeString += "10.인원 : " + temporaryTravel.getPerson().toString();
        placeString = temporaryTravel.getTendency() + placeString;

        List<String> timeTable = responseHandleService
                .parseTimeTable(responseHandleService
                        .convertOpenAiResponseToString(getTravelTimeTable(placeString)));

        for(String eachTime : timeTable) {
            TemporaryRecommend temporaryRecommend = TemporaryRecommend.builder()
                    .day(responseHandleService.parseTimeAndTravel(eachTime).get(0).trim())
                    .recommend(responseHandleService.parseTimeAndTravel(eachTime).get(1).trim())
                    .temporaryTravel(temporaryTravel)
                    .build();

            temporaryRecommendRepository.save(temporaryRecommend);
        }
        temporaryTravel.nextStep();

        List<TravelEachRecommend> travelEachRecommends = timeTable.stream()
                .map(table ->
                        TravelEachRecommend.builder()
                                .day(responseHandleService.parseTimeAndTravel(table).get(0).trim())
                                .plan(responseHandleService.parseTimeAndTravel(table).get(1).trim())
                                .build()).toList();

        return TravelRecommendResponse.builder()
                .travelEachRecommends(travelEachRecommends.subList(0, travelEachRecommends.size()-1))
                .build();
    }

    @Transactional
    public String fixTravelTimeTable(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        if(responseHandleService.convertOpenAiResponseToString(getNextStep(userRequest)).equals("0")) {
            // 만약 더 이상 여행 일정을 수정하지 않는다면 다음과 같은 로직 진행
            saveTravelRecord(userId);
            return null;
        }

        return null;
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
            case 3:
                return fixTravelTimeTable(userId, userRequest);
            default:
                break;
        }

        return new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
