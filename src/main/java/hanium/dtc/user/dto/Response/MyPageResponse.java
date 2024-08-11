package hanium.dtc.user.dto.Response;

import hanium.dtc.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
public record MyPageResponse (
    String nickname,
    Integer age,
    Boolean gender,
    Integer mbti,
    Integer image
)
{}
