package hanium.dtc.user.dto.Response;

import lombok.Builder;

@Builder
public record UserCommentResponse (
        String nickname,
        Integer image
){ }
