package hanium.dtc.security.controller;

import hanium.dtc.auth.service.AuthService;
import hanium.dtc.global.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final AuthService authService;

    @GetMapping("/callback")
    public ResponseDto<?> existUserBySerialId(@RequestParam("code") String code) {
        return ResponseDto.ok(authService.existUserBySerialId(code));
    }
}