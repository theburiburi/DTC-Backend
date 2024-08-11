package hanium.dtc.community.controller;

import hanium.dtc.global.ResponseDto;
import hanium.dtc.community.dto.request.PostRequest;
import hanium.dtc.community.dto.response.PostDetailResponse;
import hanium.dtc.community.dto.response.PostResponse;
import hanium.dtc.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseDto<?> createPost(@RequestBody PostRequest postRequest) {
        return ResponseDto.created(postService.createPost(postRequest));
    }

    @GetMapping
    public ResponseDto<List<PostResponse>> getAllPosts() {
        return ResponseDto.ok(postService.getPosts());
    }

    @GetMapping("/{postId}")
    public ResponseDto<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        return ResponseDto.ok(postService.getPostDetail(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        return ResponseDto.created(postService.updatePost(postId, postRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId) {
        return ResponseDto.created(postService.deletePost(postId));
    }

}
