package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    private String favorites;
    private String followers;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    private List<Ticket> ticketList;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<BookingFeedback> bookingFeedback;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Booking> booking;

}