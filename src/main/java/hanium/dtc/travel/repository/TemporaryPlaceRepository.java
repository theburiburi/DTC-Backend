package hanium.dtc.travel.repository;

import hanium.dtc.travel.domain.TemporaryPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryPlaceRepository extends JpaRepository<TemporaryPlace, Long> {
}
