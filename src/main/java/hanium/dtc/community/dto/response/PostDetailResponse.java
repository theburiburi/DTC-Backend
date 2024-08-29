package hanium.dtc.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.travel.dto.response.TravelRecordResponse;
import hanium.dtc.user.dto.Response.UserCommentResponse;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDetailResponse(

        @JsonProperty("user")
        UserCommentResponse user,

        @JsonProperty("travel")
        TravelRecordResponse travel,

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("comment")
        Integer comment,

        @JsonProperty("scrap")
        Integer scrap,

        @JsonProperty("postTime")
        LocalDateTime postTime,

        @JsonProperty("comments")
        List<CommentResponse> comments
) {
}


