package hanium.dtc.mypage.dto.Response;


import lombok.Builder;

@Builder
public record MyPageUpdateResponse (
        String name,
        String nickname,
        String address,
        Integer age,
        Boolean gender,
        Integer mbti,
        Integer image

){}
