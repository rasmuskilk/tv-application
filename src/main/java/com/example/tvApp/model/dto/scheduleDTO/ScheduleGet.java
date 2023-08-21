package com.example.tvApp.model.dto.scheduleDTO;

import com.example.tvApp.helpers.enums.ProgramType;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleGet(
        String dayOfTheWeek,
        LocalDate airDate,
        String programName,
        String programDescription,
        ProgramType programType,
        Integer duration,
        LocalTime startTime,
        LocalTime endTime
) {

}
