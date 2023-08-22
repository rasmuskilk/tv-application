package com.example.tvApp.model.dto.scheduleDTO;

import com.example.tvApp.helpers.enums.ProgramType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleGet(
        String dayOfTheWeek,
        LocalDate airDate,
        String programName,
        String programDescription,
        ProgramType programType,
        Integer duration,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "21:00:00")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "22:00:00")
        LocalTime endTime
) {

}
