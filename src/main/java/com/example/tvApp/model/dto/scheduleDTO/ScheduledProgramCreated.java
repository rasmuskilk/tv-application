package com.example.tvApp.model.dto.scheduleDTO;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.helpers.enums.RecurringDays;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduledProgramCreated(
        Integer id,
        String programName,
        String channelName,
        List<RecurringDays> recurringDays,
        ProgramType programType,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate startDate,
        LocalDate endDate

) {
}
