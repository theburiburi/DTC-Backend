package hanium.dtc.auth.controller;

import hanium.dtc.annotation.UserId;
import hanium.dtc.auth.dto.request.SignUpRequest;
import hanium.dtc.auth.service.AuthService;
import hanium.dtc.global.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/api/auth/login/kakao")
    public ResponseDto<?> login(@RequestParam("code") String code) {
        return ResponseDto.ok(authService.login(code));
    }

    @PatchMapping("/api/sign-up")
    public ResponseDto<?> signUp(@UserId Long userId, @RequestBody SignUpRequest signUpRequest) {
        return ResponseDto.created(authService.signUp(userId, signUpRequest));
    }

}