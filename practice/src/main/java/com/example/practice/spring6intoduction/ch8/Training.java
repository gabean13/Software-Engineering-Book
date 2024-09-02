package com.example.practice.spring6intoduction.ch8;

import java.time.LocalDateTime;

public class Training {
    private final String id;
    private final String title;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Integer reserved;
    private final Integer capacity;

    public Training(String id, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer reserved, Integer capacity) {
        this.id = id;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reserved = reserved;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Integer getReserved() {
        return reserved;
    }

    public Integer getCapacity() {
        return capacity;
    }
}
