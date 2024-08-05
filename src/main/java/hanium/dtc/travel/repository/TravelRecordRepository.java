package hanium.dtc.travel.repository;

import hanium.dtc.travel.domain.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {
}
