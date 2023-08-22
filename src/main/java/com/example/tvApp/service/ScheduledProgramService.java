package com.example.tvApp.service;

import com.example.tvApp.exceptions.ApiException;
import com.example.tvApp.helpers.ScheduleHelper;
import com.example.tvApp.helpers.ValidationHelpers;
import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.helpers.enums.RecurringDays;
import com.example.tvApp.mapper.ScheduledProgramMapper;
import com.example.tvApp.model.Channel;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.ScheduledProgram;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleForChannel;
import com.example.tvApp.model.dto.programDTO.ScheduleProgramCreate;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleForAllChannels;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleGet;
import com.example.tvApp.model.dto.scheduleDTO.ScheduledProgramCreated;
import com.example.tvApp.repository.ChannelRepository;
import com.example.tvApp.repository.ProgramRepository;
import com.example.tvApp.repository.ScheduledProgramRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ScheduledProgramService {
    private final ScheduledProgramRepository scheduledProgramRepository;
    private final ChannelRepository channelRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public ScheduledProgramService(ScheduledProgramRepository scheduledProgramRepository, ChannelRepository channelRepository, ProgramRepository programRepository) {
        this.scheduledProgramRepository = scheduledProgramRepository;
        this.channelRepository = channelRepository;
        this.programRepository = programRepository;
    }

    public List<ScheduleForAllChannels> getScheduleForAllChannels(LocalDate startDate, LocalDate endDate) {
        List<ScheduleForChannel> scheduledPrograms = scheduledProgramRepository.findAllForChannels();

        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(3); // Default to 3 days
        }

        ValidationHelpers.validateStartAndEndDates(startDate, endDate);

        return ScheduleHelper.createScheduleForAllChannels(scheduledPrograms, getDaysBetween(startDate, endDate), startDate);
    }

    public List<ScheduleGet> getScheduleByChannelId(Integer channelId, LocalDate startDate, LocalDate endDate) {
        ValidationHelpers.validateEntityExists(channelRepository.findById(channelId), "Channel");

        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(3); // Default to 3 days
        }

        ValidationHelpers.validateStartAndEndDates(startDate, endDate);

        List<ScheduleForChannel> scheduledPrograms = scheduledProgramRepository.findAllByChannelId(channelId);

        return ScheduleHelper.createSchedule(scheduledPrograms, getDaysBetween(startDate, endDate), startDate);
    }

    public List<ScheduleForAllChannels> getScheduleByProgramType(ProgramType programType, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(3); // Default to 3 days
        }

        ValidationHelpers.validateStartAndEndDates(startDate, endDate);

        List<ScheduleForChannel> scheduledPrograms = scheduledProgramRepository.findAllByProgramType(programType);

        return ScheduleHelper.createScheduleForAllChannels(scheduledPrograms, getDaysBetween(startDate, endDate), startDate);
    }

    public ScheduledProgramCreated addProgramToChannelScheduleByProgramId(Integer channelId, ScheduleProgramCreate scheduleProgramCreate) throws JsonProcessingException {
        Channel channel = ValidationHelpers.validateEntityExistsAndReturnEntity(channelRepository.findById(channelId), "Channel");
        Program program = ValidationHelpers.validateEntityExistsAndReturnEntity(programRepository.findById(scheduleProgramCreate.programId()), "Program");

        validateScheduledProgram(scheduleProgramCreate, program);

        List<ScheduleForChannel> scheduledPrograms = scheduledProgramRepository.findAllByChannelId(channelId);

        ScheduleHelper.validateAvailableTimeSlotForNewProgram(scheduleProgramCreate, scheduledPrograms, program);

        ScheduledProgram savedProgram = scheduledProgramRepository.save(
                ScheduledProgramMapper.mapToScheduledProgram(scheduleProgramCreate, program, channelId)
        );

        return ScheduledProgramMapper.mapToScheduleCreated(savedProgram, program, channel.name());
    }

    public Boolean removeProgramFromSchedule(Integer channelId, Integer programId) {
        ValidationHelpers.validateEntityExists(channelRepository.findById(channelId), "Channel");
        ValidationHelpers.validateEntityExists(programRepository.findById(programId), "Program");

        scheduledProgramRepository.deleteByProgramId(programId);

        return true;
    }

    private void validateRecurringDays(List<RecurringDays> recurringDays) {
        if ((recurringDays.contains(RecurringDays.ALL) || recurringDays.contains(RecurringDays.NONE)) && recurringDays.size() > 1) {
            throw new ApiException("Recurring days should only have one property when having ALL or NONE", HttpStatus.BAD_REQUEST);
        }
    }
    private void validateStartAndEndDate(ScheduleProgramCreate scheduleProgramCreate, Program program) {
        if (scheduleProgramCreate.recurringDays().contains(RecurringDays.NONE) && scheduleProgramCreate.startDate() == null) {
            throw new ApiException("Start date must be provided when show is non-recurring", HttpStatus.BAD_REQUEST);
        }

        if (program.numberOfEpisodes() != null && program.numberOfEpisodes() != 0 && scheduleProgramCreate.startDate() == null) {
            throw new ApiException("Start date must be provided for programs with episodes number", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateScheduledProgram(ScheduleProgramCreate scheduleProgramCreate, Program program) {
        validateRecurringDays(scheduleProgramCreate.recurringDays());
        validateStartAndEndDate(scheduleProgramCreate, program);
    }

    private Integer getDaysBetween(LocalDate startDate, LocalDate endDate) {
        int days = Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate)) + 1; // Including today

        if (days > 14) {
            throw new ApiException("Schedule can be returned for two weeks maximum", HttpStatus.BAD_REQUEST);
        }

        return days;
    }
}
