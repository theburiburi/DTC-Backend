package hanium.dtc.travel.controller;

import hanium.dtc.global.ResponseDto;
import hanium.dtc.travel.dto.request.TemporaryTravelCreateRequest;
import hanium.dtc.travel.service.TemporaryTravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TemporaryTravelController {
    private final TemporaryTravelService temporaryTravelService;

    @PostMapping("/temp-place")
    public ResponseDto<?> createTemporaryTravel(@RequestBody TemporaryTravelCreateRequest temporaryTravelCreateRequest) {
        return ResponseDto.created(temporaryTravelService.createTemporaryTravel(1L, temporaryTravelCreateRequest));
    }

    @PatchMapping("/temp-place")
    public ResponseDto<?> updateTravelPerson(@RequestParam Integer person) {
        return ResponseDto.created(temporaryTravelService.updatePersonNumber(1L, person));
    }
}
