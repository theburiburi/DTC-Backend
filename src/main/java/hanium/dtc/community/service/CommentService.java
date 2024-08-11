package hanium.dtc.community.service;

import hanium.dtc.community.domain.Comment;
import hanium.dtc.community.domain.Post;
import hanium.dtc.user.domain.User;
import hanium.dtc.community.dto.Request.CommentRequest;
import hanium.dtc.community.dto.Response.CommentResponse;
import hanium.dtc.community.repository.CommentRepository;
import hanium.dtc.community.repository.PostRepository;
import hanium.dtc.user.repository.UserRepository;
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
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        commentRepository.delete(comment);
        return true;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsInPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        return post.getComments().stream()
                .map(comment -> CommentResponse.builder()
                        .content(comment.getContent())
                        .user(User.builder()
                                .nickname(comment.getUser().getNickname())
                                .image(comment.getUser().getImage())
                                .build())
                        .like(comment.getCommentLike())
                        .commentTime(comment.getCommentTime())
                        .isReply(comment.getIsReply())
                        .build())
                .collect(Collectors.toList());
    }


}