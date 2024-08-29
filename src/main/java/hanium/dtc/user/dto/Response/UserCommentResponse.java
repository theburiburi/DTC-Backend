package hanium.dtc.user.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record UserCommentResponse (
        String nickname,
        Integer image
){ }
