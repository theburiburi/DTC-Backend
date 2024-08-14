package hanium.dtc.auth.dto.response;

import lombok.Builder;

@Builder
public record JwtTokensDto(
        String accessToken,

        String refreshToken
) {
}
