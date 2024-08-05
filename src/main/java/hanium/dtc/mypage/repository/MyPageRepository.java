package hanium.dtc.mypage.repository;

import hanium.dtc.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPageRepository extends JpaRepository<User, Long> {
}
