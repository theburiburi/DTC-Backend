package hanium.dtc.travel.controller;

import hanium.dtc.global.ResponseDto;
import hanium.dtc.travel.domain.RecordDetail;
import hanium.dtc.travel.dto.request.TravelRecordDetailRequest;
import hanium.dtc.travel.dto.request.TravelTitleRequest;
import hanium.dtc.travel.service.TravelRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TravelRecordController {
    private final TravelRecordService travelRecordService;
    @GetMapping("/mypage/record/{userId}")
    public ResponseDto<?> getTravelRecord(@PathVariable Long userId) {
        return ResponseDto.ok(travelRecordService.travelRecordList(userId));
    }

    @GetMapping("/mypage/{travelId}/{day}")
    public ResponseDto<?> getTravelDetail(@PathVariable Long travelId, @PathVariable Integer day) {
        return ResponseDto.ok(travelRecordService.travelRecordDetail(travelId, day));
    }

    @PatchMapping("/mypage/{travelId}")
    public ResponseDto<?> updateTravelTitle(@PathVariable Long travelId, @RequestBody TravelTitleRequest request) {
        return ResponseDto.created(travelRecordService.updateTravelTitle(travelId, request));
    }

    @PatchMapping("/mypage/detail/{detailId}")
    public ResponseDto<?> updateRecordDetail(@PathVariable Long detailId, @RequestBody TravelRecordDetailRequest request) {
        return ResponseDto.created(travelRecordService.updateRecordDetail(detailId, request));
    }

    @DeleteMapping("mypage/detail/{detailId}")
    public ResponseDto<?> deleteRecordDetail(@PathVariable Long detailId) {
        return ResponseDto.created(travelRecordService.deleteRecordDetail(detailId));
    }

    @DeleteMapping("mypage/record/{travelId}")
    public ResponseDto<?> deleteTravelRecord(@PathVariable Long travelId) {
        return ResponseDto.created(travelRecordService.deleteTravelRecord(travelId));
    }

}
