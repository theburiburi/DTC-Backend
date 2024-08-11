package hanium.dtc.community.dto.Response;
import hanium.dtc.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.user.dto.Response.UserCommentResponse;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CommentResponse(

        @JsonProperty("content")
        String content,

        @JsonProperty("user")
        UserCommentResponse userCommentResponse,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("comment_id")
        Long commentId,

        @JsonProperty("comment_time")
        LocalDateTime commentTime,

        @JsonProperty("is_reply")
        boolean isReply
) {
}
