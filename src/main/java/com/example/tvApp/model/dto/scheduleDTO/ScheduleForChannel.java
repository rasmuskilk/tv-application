package com.example.tvApp.model.dto.scheduleDTO;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.helpers.enums.RecurringDays;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleForChannel(
        String channelName,
        String programName,
        String programDescription,
        Integer duration,
        Integer numberOfEpisodes,
        List<RecurringDays> recurringDays,
        ProgramType programType,
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
