package com.graduation.realestateconsulting.trait;

import java.time.LocalDateTime;
import java.util.Objects;

public record TimeBlock(LocalDateTime start, LocalDateTime end) {


    public TimeBlock {
        Objects.requireNonNull(start, "Start datetime cannot be null");
        Objects.requireNonNull(end, "End datetime cannot be null");
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start datetime cannot be after end datetime.");
        }
    }

    public boolean overlaps(TimeBlock other) {
        return this.start.isBefore(other.end) && this.end.isAfter(other.start);
    }


    public boolean isValid() {
        return !start.isAfter(end) && !start.equals(end);
    }
}
