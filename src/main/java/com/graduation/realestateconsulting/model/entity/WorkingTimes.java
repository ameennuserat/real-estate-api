package com.graduation.realestateconsulting.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.graduation.realestateconsulting.model.enums.CallType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkingTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Column(nullable = false,name = 'day')
    @Column(name = "day", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "booking_type")
    private CallType callType;;

    @Column(name = "start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "expert_id", referencedColumnName = "id")
    private Expert expert;

}