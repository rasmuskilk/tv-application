package com.example.tvApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Table("scheduled_programs")
public record ScheduledProgram(
        @Id Integer id,
        Integer programId,
        Integer channelId,
        String recurringDays,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "21:00:00")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "22:00:00")
        LocalTime endTime,
        LocalDate startDate,
        LocalDate endDate
) {
}
