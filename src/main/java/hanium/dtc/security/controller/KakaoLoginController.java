package hanium.dtc.security.controller;

import hanium.dtc.global.ResponseDto;
import hanium.dtc.security.dto.KakaoUserInfoResponse;
import hanium.dtc.security.service.KakaoService;
import hanium.dtc.user.service.UserService;
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

    private final UserService userService;

    @GetMapping("/callback")
    public ResponseDto<?> existUserBySerialId(@RequestParam("code") String code) {
        return ResponseDto.ok(userService.existUserBySerialId(code));
    }
}