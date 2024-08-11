package hanium.dtc.community.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.user.domain.User;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(

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

