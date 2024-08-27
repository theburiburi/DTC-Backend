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
    public TravelRecordListResponse travelScrapList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_USER));

        TravelRecordListResponse travelRecordListResponse = TravelRecordListResponse.builder()
                .travelRecordResponses(user.getTravelRecords().stream()
                        .filter(travelRecord -> travelRecord.getIsScrap().equals(Boolean.TRUE))
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
    public ScrapResponse toggleScrapTravelRecord(Long travelId){

        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(()-> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        Post relatedPost = postRepository.findByTravelId(travelRecord.getId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        boolean isScrapped;

        if(!travelRecord.getIsScrap()){
            TravelRecord newTravelRecord = new TravelRecord(
                    travelRecord.getTitle(),
                    travelRecord.getPlace(),
                    travelRecord.getDepartAt(),
                    travelRecord.getArriveAt(),
                    travelRecord.getImageUrl(),
                    travelRecord.getUser()
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
            relatedPost.incrementScrap();
            travelRecordRepository.save(newTravelRecord);
            travelRecord.setIsScrap(true);
            isScrapped = true;
        }
        else{
            relatedPost.decrementScrap();
            travelRecordRepository.delete(travelRecord);
            travelRecord.setIsScrap(false);
            isScrapped = false;
        }
        travelRecordRepository.save(travelRecord);
        postRepository.save(relatedPost);

        return ScrapResponse.builder()
                .isScrapped(isScrapped)
                .scrap(relatedPost.getScrap())
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

