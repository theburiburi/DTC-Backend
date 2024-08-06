package hanium.dtc.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.travel.domain.RecordDetail;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TravelRecordDetailResponse (
        @JsonProperty("travel")
        TravelDetailResponse travelDetailResponse,

        @JsonProperty("travel_details")
        List<RecordDetailResponse> recordDetailResponses
) {
}
