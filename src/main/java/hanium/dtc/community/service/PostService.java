package hanium.dtc.community.service;

import hanium.dtc.user.domain.User;
import hanium.dtc.community.domain.Post;
import hanium.dtc.travel.domain.TravelRecord;
import hanium.dtc.community.dto.request.PostRequest;
import hanium.dtc.community.dto.response.CommentResponse;
import hanium.dtc.community.dto.response.PostDetailResponse;
import hanium.dtc.community.dto.response.PostResponse;
import hanium.dtc.travel.dto.response.TravelRecordResponse;
import hanium.dtc.community.repository.PostRepository;
import hanium.dtc.user.dto.Response.UserCommentResponse;
import hanium.dtc.user.repository.UserRepository;
import hanium.dtc.travel.repository.TravelRecordRepository;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TravelRecordRepository travelRecordRepository;

    @Transactional
    public boolean createPost(PostRequest postRequest) {

        TravelRecord travelRecord = null;
        if (postRequest.travelId() != null) {
            travelRecord = travelRecordRepository.findById(postRequest.travelId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));
        }
        Post post = new Post(postRequest.title(), postRequest.content(), travelRecord);
        postRepository.save(post);
        return true;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPosts() {
        return postRepository.findAll().stream()
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
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        TravelRecordResponse travelRecordResponse = null;
        if (post.getTravelRecord() != null) {
            TravelRecord travelRecord = post.getTravelRecord();
            travelRecordResponse = TravelRecordResponse.builder()
                    .title(travelRecord.getTitle())
                    .place(travelRecord.getPlace())
                    .departAt(travelRecord.getDepartAt())
                    .arriveAt(travelRecord.getArriveAt())
                    .imageUrl(travelRecord.getImageUrl())
                    .build();
        }
        return PostDetailResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .user(User.builder()
                        .nickname(post.getUser().getNickname())
                        .image(post.getUser().getImage())
                        .build())
                .postTime(post.getPostTime())
                .like(post.getPostLike())
                .comment(post.getComment())
                .scrap(post.getScrap())
                .travel(travelRecordResponse)
                .comments(post.getComments().stream()
                        .map(comment -> CommentResponse.builder()
                                .content(comment.getContent())
                                .userCommentResponse(UserCommentResponse.builder()
                                        .nickname(comment.getUser().getNickname())
                                        .image(comment.getUser().getImage())
                                        .build())
                                .commentTime(comment.getCommentTime())
                                .isReply(comment.getIsReply())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public boolean updatePost(Long postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        TravelRecord travelRecord = null;
        if (postRequest.travelId() != null) {
            travelRecord = travelRecordRepository.findById(postRequest.travelId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));
        }

        post.updatePost(postRequest.title(), postRequest.content(), travelRecord);
        return true;
    }

    @Transactional
    public boolean deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));
        postRepository.delete(post);
        return true;
    }
}
