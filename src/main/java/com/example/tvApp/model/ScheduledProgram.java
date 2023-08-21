package com.example.tvApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Table("scheduled_programs")
public record ScheduledProgram(
        @Id Integer id,
        Integer programId,
        Integer channelId,
        String recurringDays,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate startDate,
        LocalDate endDate
) {
}
