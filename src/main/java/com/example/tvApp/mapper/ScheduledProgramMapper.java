package com.example.tvApp.mapper;

import com.example.tvApp.helpers.enums.RecurringDays;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.ScheduledProgram;
import com.example.tvApp.model.dto.programDTO.ScheduleProgramCreate;
import com.example.tvApp.model.dto.scheduleDTO.ScheduledProgramCreated;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduledProgramMapper {
    public static ScheduledProgramCreated mapToScheduleCreated(ScheduledProgram scheduledProgram, Program program, String channelName) throws JsonProcessingException {
        return new ScheduledProgramCreated(
                scheduledProgram.id(),
                program.name(),
                channelName,
                new ObjectMapper().readValue(scheduledProgram.recurringDays(), new TypeReference<>() {
                }),
                program.programType(),
                scheduledProgram.startTime(),
                scheduledProgram.endTime(),
                scheduledProgram.startDate(),
                scheduledProgram.endDate()
        );
    }

    public static ScheduledProgram mapToScheduledProgram(ScheduleProgramCreate scheduleProgramCreate, Program program, Integer channelId) throws JsonProcessingException {
        List<RecurringDays> distinctRecurringDays = scheduleProgramCreate.recurringDays().stream().distinct().toList();

        LocalTime endTime = scheduleProgramCreate.startTime().plusMinutes(program.duration());
        LocalDate endDate = getEndDate(distinctRecurringDays, program.numberOfEpisodes(), scheduleProgramCreate.startDate());

        return new ScheduledProgram(
                null,
                program.id(),
                channelId,
                new ObjectMapper().writeValueAsString(distinctRecurringDays),
                scheduleProgramCreate.startTime(),
                endTime,
                scheduleProgramCreate.startDate(),
                endDate
        );
    }

    private static LocalDate getEndDate(List<RecurringDays> distinctRecurringDays, Integer numberOfEpisodes, LocalDate startDate) {
        if (distinctRecurringDays.contains(RecurringDays.NONE)) {
            return startDate;
        }

        return numberOfEpisodes != null ? startDate.plusDays(numberOfEpisodes) : null;
    }
}
