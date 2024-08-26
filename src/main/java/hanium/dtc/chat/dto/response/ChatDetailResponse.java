package hanium.dtc.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.chat.domain.ChatType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatDetailResponse(

        @JsonProperty("user_id")
        Long senderId,

        String content,

        @JsonProperty("chat_time")
        LocalDateTime chatTime,

        @JsonProperty("type")
        ChatType chatType
) {
}
