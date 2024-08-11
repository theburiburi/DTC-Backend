package hanium.dtc.community.repository;

import hanium.dtc.community.domain.Comment;
import hanium.dtc.travel.domain.RecordDetail;
import hanium.dtc.travel.domain.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostId(Long postId);
}
