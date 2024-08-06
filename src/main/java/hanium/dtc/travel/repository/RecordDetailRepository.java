package hanium.dtc.travel.repository;

import hanium.dtc.travel.domain.RecordDetail;
import hanium.dtc.travel.domain.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {
    Optional<List<RecordDetail>> findByTravelRecordAndDay(TravelRecord travelRecord, Integer day);
}
