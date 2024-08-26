package hanium.dtc.chat.repository;

import hanium.dtc.chat.domain.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {
}