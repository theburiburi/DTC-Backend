package hanium.dtc.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.travel.dto.response.TravelRecordResponse;
import hanium.dtc.user.domain.User;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDetailResponse(

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content,

        @JsonProperty("user")
        User user,

        @JsonProperty("post_time")
        LocalDateTime postTime,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("comment")
        Integer comment,

        @JsonProperty("scrap")
        Integer scrap,

        @JsonProperty("is_mine")
        Boolean isMine,

        @JsonProperty("travel")
        TravelRecordResponse travel,

        @JsonProperty("comments")
        List<CommentResponse> comments
) {
}


