package hanium.dtc.mypage.service;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.mypage.dto.Request.MyPageUpdateRequest;
import hanium.dtc.mypage.dto.Response.MyPageResponse;
import hanium.dtc.mypage.repository.MyPageRepository;
import hanium.dtc.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyPageService {
    private final MyPageRepository myPageRepository;
    public MyPageService(MyPageRepository myPageRepository) {
        this.myPageRepository = myPageRepository;
    }

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(Long userId){

        User user = myPageRepository.findById(userId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_USER));
        MyPageResponse myPageResponse = MyPageResponse.builder()
                .age(user.getAge())
                .image(user.getImage())
                .mbti(user.getMbti())
                .gender(user.getGender())
                .nickname(user.getNickname())
                .build();

        return myPageResponse;
    }

    @Transactional
    public void updateMyPage(MyPageUpdateRequest request, Long userId){
        User user = myPageRepository.findById(userId)
                .orElseThrow(IllegalAccessError::new);

        user.update(request);
    }
}
