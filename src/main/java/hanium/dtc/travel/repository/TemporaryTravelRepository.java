package hanium.dtc.travel.repository;

import hanium.dtc.travel.domain.TemporaryTravel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryTravelRepository extends JpaRepository<TemporaryTravel, Long> {
}
