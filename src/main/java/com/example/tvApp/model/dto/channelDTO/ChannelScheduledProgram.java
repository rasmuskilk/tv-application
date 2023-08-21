package com.example.tvApp.model.dto.channelDTO;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.helpers.enums.RecurringDays;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ChannelScheduledProgram(
        String channelName,
        String programName,
        String programDescription,
        Integer duration,
        Integer numberOfEpisodes,
        List<RecurringDays> recurringDays,
        ProgramType programType,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate startDate,
        LocalDate endDate
) {
}
