package hanium.dtc.community.service;

import hanium.dtc.community.domain.Post;
import hanium.dtc.community.dto.response.PostResponse;
import hanium.dtc.community.repository.PostRepository;
import hanium.dtc.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> getFreeBoardPosts(int page) {
        int size = 10;
        PageRequest pageable = PageRequest.of(page - 1, size);
        return postRepository.findByIsTravel(false, pageable)
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
                        .build());
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getTravelBoardPosts(int page) {
        int size = 10;
        PageRequest pageable = PageRequest.of(page - 1, size);
        return postRepository.findByIsTravel(true, pageable)
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
                        .build());
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getHotBoardPosts(int page) {
        int size = 10;
        int likeThreshold = 10;
        PageRequest pageable = PageRequest.of(page - 1, size);
        return postRepository.findByPostLikeGreaterThanEqual(likeThreshold, pageable)
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
                        .build());
    }
}

