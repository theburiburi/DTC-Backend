package hanium.dtc.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import hanium.dtc.user.domain.User;
import hanium.dtc.user.dto.Response.UserCommentResponse;
import lombok.Builder;


import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HotResponse(

        @JsonProperty("post_id")
        Long postId,

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("user")
        UserCommentResponse user,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("place")
        String place

) {
}
