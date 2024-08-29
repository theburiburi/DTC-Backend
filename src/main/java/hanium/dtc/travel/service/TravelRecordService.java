package hanium.dtc.travel.service;

import hanium.dtc.community.domain.Post;
import hanium.dtc.community.repository.PostRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.travel.domain.RecordDetail;
import hanium.dtc.travel.domain.TravelRecord;
import hanium.dtc.travel.dto.request.TravelRecordDetailRequest;
import hanium.dtc.travel.dto.request.TravelTitleRequest;
import hanium.dtc.travel.dto.response.*;
import hanium.dtc.travel.repository.RecordDetailRepository;
import hanium.dtc.travel.repository.TravelRecordRepository;
import hanium.dtc.user.domain.User;
import hanium.dtc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelRecordService {
    private final TravelRecordRepository travelRecordRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public TravelRecordListResponse travelRecordList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_USER));

        LocalDate filterDate = LocalDate.now();

        TravelRecordListResponse travelRecordListResponse = TravelRecordListResponse.builder()
                .travelRecordResponses(user.getTravelRecords().stream()
                        .filter(travelRecord -> travelRecord.getDepartAt().isBefore(filterDate)
                                && travelRecord.getIsScrap().equals(Boolean.FALSE))
                        .map(travelRecord -> TravelRecordResponse.builder()
                                .title(travelRecord.getTitle().toString())
                                .place(travelRecord.getPlace().toString())
                                .departAt(travelRecord.getDepartAt())
                                .arriveAt(travelRecord.getArriveAt())
                                .imageUrl(travelRecord.getImageUrl())
                                .build())
                        .toList()).build();
        return travelRecordListResponse;
    }

    @Transactional(readOnly = true)
    public TravelRecordListResponse travelPlanList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_USER));

        LocalDate filterDate = LocalDate.now();

        TravelRecordListResponse travelRecordListResponse = TravelRecordListResponse.builder()
                .travelRecordResponses(user.getTravelRecords().stream()
                        .filter(travelRecord -> travelRecord.getDepartAt().isAfter(filterDate)
                                && travelRecord.getIsScrap().equals(Boolean.FALSE))
                        .map(travelRecord -> TravelRecordResponse.builder()
                                .title(travelRecord.getTitle().toString())
                                .place(travelRecord.getPlace().toString())
                                .departAt(travelRecord.getDepartAt())
                                .arriveAt(travelRecord.getArriveAt())
                                .imageUrl(travelRecord.getImageUrl())
                                .build())
                        .toList()).build();
        return travelRecordListResponse;
    }

    @Transactional(readOnly = true)
    public List<TravelRecordResponse> getMyScrapList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return travelRecordRepository.findByUser(user).stream()
                .filter(travelRecord -> travelRecord.getIsScrap().equals(Boolean.TRUE))
                .map(travelRecord -> TravelRecordResponse.builder()
                        .travelRecordId(travelRecord.getId())
                        .title(travelRecord.getTitle())
                        .place(travelRecord.getPlace())
                        .departAt(travelRecord.getDepartAt())
                        .arriveAt(travelRecord.getArriveAt())
                        .imageUrl(travelRecord.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TravelRecordDetailResponse travelRecordDetail(Long travelId, Integer day) {
        TravelRecord travelRecord = travelRecordRepository.findById(travelId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_TRAVEL));
        List<RecordDetail> recordDetails = recordDetailRepository.findByTravelRecordAndDay(travelRecord, day)
                .orElseThrow(()-> new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        TravelRecordDetailResponse travelRecordDetailResponse = TravelRecordDetailResponse.builder()
                .travelDetailResponse(TravelDetailResponse.builder()
                        .title(travelRecord.getTitle())
                        .departAt(travelRecord.getDepartAt())
                        .arriveAt(travelRecord.getArriveAt())
                        .build())
                .recordDetailResponses(recordDetails.stream()
                        .map(RecordDetail-> RecordDetailResponse.builder()
                                .title(RecordDetail.getTitle().toString())
                                .thema(RecordDetail.getThema().toString())
                                .detailAddress(RecordDetail.getDetailAddress().toString())
                                .startAt(RecordDetail.getStartAt())
                                .endAt(RecordDetail.getEndAt())
                                .build())
                        .toList()).build();
        return travelRecordDetailResponse;
    }

    @Transactional
    public ScrapResponse toggleScrapTravelRecord(Long travelId, Long userId){

        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Optional<TravelRecord> existingScrap = travelRecordRepository.findByUserAndId(user, travelId);

        boolean isScrapped;
        if (existingScrap.isPresent()) {
            travelRecordRepository.delete(existingScrap.get());
            travelRecord.getPost().decrementScrap();
            isScrapped = false;
        } else {
            TravelRecord newTravelRecord = new TravelRecord(
                    travelRecord.getTitle(),
                    travelRecord.getPlace(),
                    travelRecord.getDepartAt(),
                    travelRecord.getArriveAt(),
                    travelRecord.getImageUrl(),
                    user,
                    travelRecord.getPost()
            );

            for (RecordDetail recordDetails : travelRecord.getRecordDetails()){
                RecordDetail newDetail = new RecordDetail(
                        recordDetails.getTitle(),
                        recordDetails.getThema(),
                        recordDetails.getDetailAddress(),
                        recordDetails.getLat(),
                        recordDetails.getLon(),
                        recordDetails.getStartAt(),
                        recordDetails.getEndAt(),
                        recordDetails.getDay(),
                        newTravelRecord
                );
                newTravelRecord.getRecordDetails().add(newDetail);
            }

            travelRecordRepository.save(newTravelRecord);
            travelRecord.getPost().incrementScrap();
            isScrapped = true;
        }

        postRepository.save(travelRecord.getPost());

        return ScrapResponse.builder()
                .isScrapped(isScrapped)
                .scrap(travelRecord.getPost().getScrap())
                .build();
    }

    @Transactional
    public ScrapResponse removeScrap(Long travelId, Long userId) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        TravelRecord travelRecord = travelRecordRepository.findByUserAndId(user, travelId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        Post post = travelRecord.getPost();

        travelRecordRepository.delete(travelRecord);
        post.decrementScrap();

        postRepository.save(post);

        return ScrapResponse.builder()
                .isScrapped(false)
                .scrap(post.getScrap())
                .build();
    }

    @Transactional
    public boolean updateTravelTitle(Long travelId, TravelTitleRequest request) {
        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        travelRecord.updateTitle(request.title());
        travelRecordRepository.save(travelRecord);
        return Boolean.TRUE;
    }

    @Transactional
    public boolean updateRecordDetail(Long detailId, TravelRecordDetailRequest request) {
        RecordDetail recordDetail = recordDetailRepository.findById(detailId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        recordDetail.updateDetail(
                request.title(),
                request.thema(),
                request.detailAddress(),
                request.startAt(),
                request.endAt()
        );

        recordDetailRepository.save(recordDetail);
        return Boolean.TRUE;
    }


    @Transactional
    public boolean deleteRecordDetail(Long detailId) {
        RecordDetail recordDetail = recordDetailRepository.findById(detailId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        recordDetailRepository.delete(recordDetail);
        return Boolean.TRUE;
    }

    @Transactional
    public boolean deleteTravelRecord(Long travelId) {
        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        travelRecordRepository.delete(travelRecord);
        return Boolean.TRUE;
    }

}

