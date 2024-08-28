package hanium.dtc.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.user.domain.User;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record PostResponse(

        @JsonProperty("postId")
        Long postId,

        @JsonProperty("title")
        String title,

        @JsonProperty("user")
        User user,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("comment")
        Integer comment,

        @JsonProperty("scrap")
        Integer scrap,

        @JsonProperty("post_time")
        LocalDateTime postTime

) {
}

