package hanium.dtc.community.controller;

import hanium.dtc.global.ResponseDto;
import hanium.dtc.community.dto.Request.CommentRequest;
import hanium.dtc.community.dto.Response.CommentResponse;
import hanium.dtc.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        return ResponseDto.created(commentService.createComment(postId, commentRequest));
    }

    @PatchMapping("/{postId}/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return ResponseDto.created(commentService.updateComment(commentId, commentRequest));
    }

    @DeleteMapping("/{postId}/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId) {
        return ResponseDto.created(commentService.deleteComment(commentId));
    }

    @GetMapping("/{postId}")
    public ResponseDto<List<List<CommentResponse>>> getCommentsInPost(@PathVariable Long postId) {
        List<List<CommentResponse>> comments = commentService.getCommentsInPost(postId);
        return ResponseDto.ok(comments);
    }
}




