package hanium.dtc.user.service;

import hanium.dtc.security.dto.KakaoUserInfoResponse;
import hanium.dtc.security.service.KakaoService;
import hanium.dtc.user.domain.User;
import hanium.dtc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    @Transactional
    public Boolean existUserBySerialId(String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoService.getUserInfo(accessToken);
        Long serialId = kakaoUserInfoResponse.getId();

        User user = userRepository.findBySerialId(serialId).orElseGet(
                () -> userRepository.save(new User(serialId))
        );

        return user.getNickname() == null;
    }

}
