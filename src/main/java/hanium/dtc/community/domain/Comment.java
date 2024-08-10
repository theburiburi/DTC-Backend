package hanium.dtc.community.domain;

import hanium.dtc.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_like")
    private Integer commentLike;

    @Column(name = "is_mine")
    private Boolean isMine;

    @Column(name = "is_reply")
    private Boolean isReply;

    @Column(name = "comment_time")
    private LocalDateTime commentTime;

    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String content, Boolean isReply, Long commentId, Post post) {
        this.content = content;
        this.commentLike = 0;
        this.isMine = true; // 기본값으로 설정
        this.isReply = isReply;
        this.commentTime = LocalDateTime.now();
        this.commentId = commentId;
        this.post = post;

    }

    public void updateContent(String content) {
        this.content = content;
    }
}
