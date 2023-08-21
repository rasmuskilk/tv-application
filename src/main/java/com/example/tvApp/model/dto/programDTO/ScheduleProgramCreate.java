package com.example.tvApp.model.dto.programDTO;

import com.example.tvApp.helpers.enums.RecurringDays;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleProgramCreate(
        Integer programId,
        List<RecurringDays> recurringDays,
        LocalTime startTime,
        LocalDate startDate
) {
}
