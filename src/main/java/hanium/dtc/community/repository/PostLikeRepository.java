package hanium.dtc.community.repository;

import hanium.dtc.community.domain.Post;
import hanium.dtc.community.domain.PostLike;
import hanium.dtc.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void existsByUserAndPost(User user, Post post);
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
