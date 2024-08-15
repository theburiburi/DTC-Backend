package hanium.dtc.user.controller;

import hanium.dtc.annotation.UserId;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.user.dto.Request.MyPageUpdateRequest;
import hanium.dtc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/mypage")
    public ResponseDto<?> getMyPage(@UserId Long userId){
        return ResponseDto.ok(userService.getMyPage(userId));
    }

    @GetMapping("/api/mypage/edit-profile")
    public ResponseDto<?> getMyPageUpdate(@UserId Long userId){
        return ResponseDto.ok(userService.getMyPageUpdate(userId));
    }

    @PatchMapping("/api/mypage/edit-profile")
    public ResponseDto<?> updateMyPage(@RequestBody MyPageUpdateRequest request, @UserId Long userId){
        userService.updateMyPage(request, userId);
        return ResponseDto.ok(null);
    }
}