package com.graduation.realestateconsulting.model.entity;


import com.graduation.realestateconsulting.model.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    @Column(unique = true)
    private String roomKey;

    private RoomStatus status;


    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<Message> messages;

    @CreationTimestamp
    private LocalDate createdAt;


    @PrePersist
    public void generateRoomKey() {
        Long id1 = user1.getId();
        Long id2 = user2.getId();
        if (id1 == null || id2 == null) {
            throw new IllegalStateException("Users must be set before saving the Room");
        }
        if (id1 > id2) {
            Long temp = id1;
            id1 = id2;
            id2 = temp;
        }
        this.roomKey = id1 + "_" + id2;
    }
}
