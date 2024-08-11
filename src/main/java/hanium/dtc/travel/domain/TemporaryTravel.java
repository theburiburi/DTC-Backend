package hanium.dtc.travel.domain;

import hanium.dtc.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporary_travel")
public class TemporaryTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "depart_at")
    private LocalDate departAt;

    @Column(name = "arrive_at")
    private LocalDate arriveAt;

    @Column(name = "person")
    private Integer person;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "temporaryTravel")
    private List<TemporaryPlace> temporaryPlaces = new ArrayList<>();

    @Builder
    public TemporaryTravel(LocalDate departAt, LocalDate arriveAt, User user) {
        this.departAt = departAt;
        this.arriveAt = arriveAt;
        this.user = user;
        this.temporaryPlaces = null;
    }

    public void updatePerson(Integer person) {
        this.person = person;
    }
}
