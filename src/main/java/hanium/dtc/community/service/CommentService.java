package hanium.dtc.community.service;

import hanium.dtc.community.domain.Comment;
import hanium.dtc.community.domain.Post;
import hanium.dtc.community.dto.request.CommentRequest;
import hanium.dtc.community.dto.response.CommentResponse;
import hanium.dtc.community.repository.CommentRepository;
import hanium.dtc.community.repository.PostRepository;
import hanium.dtc.user.dto.Response.UserCommentResponse;
import hanium.dtc.user.repository.UserRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean createComment(Long postId, CommentRequest commentRequest) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        Comment comment = new Comment(
                commentRequest.content(),
                commentRequest.isReply(),
                commentRequest.commentId(),
                post
        );
        commentRepository.save(comment);
        return true;
    }

    @Transactional
    public boolean updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_COMMENT));

        comment.updateContent(commentRequest.content());
        commentRepository.save(comment);
        return true;
    }

    @Transactional
    public boolean deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_COMMENT));

        commentRepository.delete(comment);
        return true;
    }

    @Transactional(readOnly = true)
    public List<List<CommentResponse>> getCommentsInPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        Map<Long, List<CommentResponse>> groupedComments = new HashMap<>();

        List<Comment> comments = commentRepository.findByPostId(postId).orElse(Collections.emptyList());

        comments.forEach(comment -> {
            UserCommentResponse userCommentResponse = UserCommentResponse.builder()
                    .nickname(comment.getUser().getNickname())
                    .image(comment.getUser().getImage())
                    .build();

            CommentResponse response = CommentResponse.builder()
                    .content(comment.getContent())
                    .userCommentResponse(UserCommentResponse.builder()
                            .nickname(comment.getUser().getNickname())
                            .image(comment.getUser().getImage())
                            .build())
                    .like(comment.getCommentLike())
                    .commentTime(comment.getCommentTime())
                    .isReply(comment.getIsReply())
                    .build();

            if (!comment.getIsReply()) {
                groupedComments.put(comment.getId(), new ArrayList<>(List.of(response)));
            } else {
                groupedComments.computeIfPresent(comment.getCommentId(), (key, list) -> {
                    list.add(response);
                    return list;
                });
            }
        });

        return new ArrayList<>(groupedComments.values());
    }


}