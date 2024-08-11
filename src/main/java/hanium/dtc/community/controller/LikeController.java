package hanium.dtc.community.controller;

import hanium.dtc.community.dto.response.LikeResponse;
import hanium.dtc.community.service.LikeService;
import hanium.dtc.user.domain.User;
import hanium.dtc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;
    private final UserRepository userRepository;

    @PostMapping("/posts/{postId}/like")
    public LikeResponse togglePostLike(@PathVariable Long postId, @RequestParam Long userId) {
        boolean isLiked = likeService.togglePostLike(postId, userId);
        return LikeResponse.builder()
                .isLiked(isLiked)
                .build();
    }

    @PostMapping("/comments/{commentId}/like")
    public LikeResponse toggleCommentLike(@PathVariable Long commentId, @RequestParam Long userId) {
        boolean isLiked = likeService.toggleCommentLike(commentId, userId);
        return LikeResponse.builder()
                .isLiked(isLiked)
                .build();
    }
}


