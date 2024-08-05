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

    @GetMapping("/api/mypage")
    public ResponseDto<?> getMyPage(){
        return ResponseDto.ok(myPageService.getMyPage(1L));
    }

    @GetMapping("/api/mypage/edit-profile")
    public ResponseDto<?> getMyPageUpdate(){
        return ResponseDto.ok(myPageService.getMyPageUpdate(1L));
    }

    @PatchMapping("/api/mypage/edit-profile")
    public ResponseDto<?> updateMyPage(@RequestBody MyPageUpdateRequest request){
        myPageService.updateMyPage(request, 1L);
        return ResponseDto.ok(null);
    }
}