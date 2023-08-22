package com.example.tvApp.model.dto.programDTO;

import com.example.tvApp.helpers.enums.RecurringDays;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleProgramCreate(
        Integer programId,
        List<RecurringDays> recurringDays,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "21:00:00")
        LocalTime startTime,
        LocalDate startDate
) {
}
