package hanium.dtc.travel.domain;

import hanium.dtc.community.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "record_detail")
public class RecordDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "thema")
    private String thema;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "day")
    private Integer day;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private TravelRecord travelRecord;

    public void updateDetail(String title, String thema, String detailAddress,
                             LocalDateTime startAt, LocalDateTime endAt){
        this.title = title;
        this.thema = thema;
        this.detailAddress = detailAddress;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public RecordDetail(String title, String thema, String detailAddress,
                        Double lat, Double lon, LocalDateTime startAt, LocalDateTime endAt,
                        Integer day, TravelRecord travelRecord) {
        this.title = title;
        this.thema = thema;
        this.detailAddress = detailAddress;
        this.lat = lat;
        this.lon = lon;
        this.startAt = startAt;
        this.endAt = endAt;
        this.day = day;
        this.travelRecord = travelRecord;
    }

}

