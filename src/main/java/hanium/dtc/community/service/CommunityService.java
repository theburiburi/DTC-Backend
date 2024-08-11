package hanium.dtc.community.service;

import hanium.dtc.community.domain.Post;
import hanium.dtc.community.dto.response.PostResponse;
import hanium.dtc.community.repository.PostRepository;
import hanium.dtc.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> getFreeBoardPosts() {
        return postRepository.findAll().stream()
                .filter(post -> Boolean.FALSE.equals(post.getIsTravel())) // 자유 게시판의 경우 여행 게시물은 제외
                .map(post -> PostResponse.builder()
                        .title(post.getTitle())
                        .user(User.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .like(post.getPostLike())
                        .comment(post.getComment())
                        .scrap(post.getScrap())
                        .postTime(post.getPostTime())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getTravelBoardPosts() {
        return postRepository.findAll().stream()
                .filter(Post::getIsTravel) // 여행 게시물만 조회
                .map(post -> PostResponse.builder()
                        .title(post.getTitle())
                        .user(User.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .like(post.getPostLike())
                        .comment(post.getComment())
                        .scrap(post.getScrap())
                        .postTime(post.getPostTime())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getHotBoardPosts(int likeThreshold) {
        return postRepository.findAll().stream()
                .filter(post -> post.getPostLike() >= likeThreshold) // 특정 좋아요 수 이상인 게시물 조회
                .map(post -> PostResponse.builder()
                        .title(post.getTitle())
                        .user(User.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .like(post.getPostLike())
                        .comment(post.getComment())
                        .scrap(post.getScrap())
                        .postTime(post.getPostTime())
                        .build())
                .collect(Collectors.toList());
    }
}

