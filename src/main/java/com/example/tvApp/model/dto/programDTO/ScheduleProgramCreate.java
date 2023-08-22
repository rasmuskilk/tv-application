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
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        @Schema(type = "string", format = "time") // Indicate the type is time
        @JsonFormat(pattern = "HH:mm:ss") // Specify the desired format
        LocalTime startTime,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        @Schema(type = "string", format = "time") // Indicate the type is time
        @JsonFormat(pattern = "HH:mm:ss") // Specify the desired format
        LocalDate startDate
) {
}
