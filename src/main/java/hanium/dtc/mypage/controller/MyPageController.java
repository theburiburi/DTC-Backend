package hanium.dtc.mypage.controller;

import hanium.dtc.global.ResponseDto;
import hanium.dtc.mypage.dto.Request.MyPageUpdateRequest;
import hanium.dtc.mypage.dto.Response.MyPageResponse;
import hanium.dtc.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/api/mypage/{userId}")
    public ResponseDto<?> getMyPage(@PathVariable Long userId){
        return ResponseDto.ok(myPageService.getMyPage(userId));
    }

    @GetMapping("/api/mypage/edit-profile/{userId}")
    public ResponseDto<?> getMyPageUpdate(@PathVariable Long userId){
        return ResponseDto.ok(myPageService.getMyPageUpdate(userId));
    }

    @PatchMapping("/api/mypage/edit-profile/{userId}")
    public ResponseDto<?> updateMyPage(@RequestBody MyPageUpdateRequest request, @PathVariable Long userId){
        myPageService.updateMyPage(request, userId);
        return ResponseDto.ok(null);
    }
}