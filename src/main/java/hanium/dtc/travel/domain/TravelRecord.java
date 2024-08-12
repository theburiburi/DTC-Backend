package hanium.dtc.travel.domain;

import hanium.dtc.community.domain.Post;
import hanium.dtc.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "travel_record")
public class TravelRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "place")
    private String place;

    @Column(name = "depart_at")
    private LocalDate departAt;

    @Column(name = "arrive_at")
    private LocalDate arriveAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_scrap")
    private Boolean isScrap;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "travelRecord")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "travelRecord", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<RecordDetail> recordDetails = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public TravelRecord(String title, String place, LocalDate departAt, LocalDate arriveAt, String imageUrl, User user) {
        this.title = title;
        this.place = place;
        this.departAt = departAt;
        this.arriveAt = arriveAt;
        this.imageUrl = imageUrl;
        this.user = user;
        this.isScrap = true;
    }
}

