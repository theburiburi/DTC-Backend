package hanium.dtc.community.controller;

import hanium.dtc.community.dto.response.LikeResponse;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.community.service.LikeService;
import hanium.dtc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;
    private final UserRepository userRepository;

    @PostMapping("/community/{postId}/like")
    public ResponseDto<LikeResponse> togglePostLike(@PathVariable Long postId) {

        boolean isLiked = likeService.togglePostLike(postId);
        int like = likeService.getPostLike(postId);

        LikeResponse response = LikeResponse.builder()
                .isLiked(isLiked)
                .like(like)
                .build();

        return ResponseDto.ok(response);
    }

    @PostMapping("/comments/{postId}/{commentId}/like")
    public ResponseDto<LikeResponse> toggleCommentLike(@PathVariable Long postId, @PathVariable Long commentId) {
        boolean isLiked = likeService.toggleCommentLike(commentId);
        int like = likeService.getCommentLike(commentId);

        LikeResponse response = LikeResponse.builder()
                .isLiked(isLiked)
                .like(like)
                .build();

        return ResponseDto.ok(response);
    }
}


