package hanium.dtc.travel.repository;

import hanium.dtc.travel.domain.TemporaryRecommend;
import hanium.dtc.travel.domain.TemporaryTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemporaryRecommendRepository extends JpaRepository<TemporaryRecommend, Long> {
    @Query("SELECT recommend FROM TemporaryRecommend WHERE temporaryTravel = :travelId")
    List<String> findAllRecommendByTemporaryTravel(@Param("travelId") TemporaryTravel temporaryTravel);
}
