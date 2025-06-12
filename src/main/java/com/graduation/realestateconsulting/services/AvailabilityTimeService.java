package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.AvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.request.AvailableTimeToBookingRequest;
import com.graduation.realestateconsulting.trait.TimeBlock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AvailabilityTimeService {

    List<LocalTime> findAvailablePeriods(AvailableTimeToBookingRequest availableTimeToBookingRequest);
    List<TimeBlock> calculateEffectiveAvailability(Long expertId, LocalDate date);
    List<TimeBlock> getBookedObstacles(Long expertId, LocalDate date);
    List<TimeBlock> mergeAndSortTimeBlocks(List<TimeBlock> blocks);
    List<LocalTime> filterAndSortFuturePeriods(List<LocalTime> generatedSlots, LocalDate queryDate, LocalDateTime currentDateTime);
}
