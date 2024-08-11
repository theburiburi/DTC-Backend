package hanium.dtc.travel.repository;

import hanium.dtc.travel.domain.TemporaryTravel;
import hanium.dtc.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemporaryTravelRepository extends JpaRepository<TemporaryTravel, Long> {
    TemporaryTravel findByUser(User user);
}
