package hanium.dtc.chat.repository;

import hanium.dtc.chat.domain.ChatDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatDetailRepository extends JpaRepository<ChatDetail, Long> {
}
