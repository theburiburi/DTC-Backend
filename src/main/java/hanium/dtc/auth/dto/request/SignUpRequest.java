package hanium.dtc.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignUpRequest(
        @JsonProperty("name")
        String name,

        @JsonProperty("nickname")
        String nickname,

        @JsonProperty("age")
        Integer age,

        @JsonProperty("address")
        String address,

        @JsonProperty("gender")
        Boolean gender,

        @JsonProperty("mbti")
        Integer mbti,

        @JsonProperty("image")
        Integer image
) {
}
