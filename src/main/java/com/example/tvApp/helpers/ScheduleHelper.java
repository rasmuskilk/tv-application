package com.example.tvApp.helpers;

import com.example.tvApp.exceptions.ApiException;
import com.example.tvApp.helpers.enums.RecurringDays;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.dto.channelDTO.ChannelScheduledProgram;
import com.example.tvApp.model.dto.programDTO.ScheduleProgramCreate;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleForAllChannels;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleGet;
import org.springframework.http.HttpStatus;

import java.time.*;
import java.util.*;

public class ScheduleHelper {
    public static List<ScheduleForAllChannels> createScheduleForAllChannels(List<ChannelScheduledProgram> scheduledPrograms, Integer days, LocalDate startDate) {
        List<ScheduleForAllChannels> schedule = new ArrayList<>();

        for (ChannelScheduledProgram scheduledProgram : scheduledPrograms) {
            schedule.add(new ScheduleForAllChannels(
                    scheduledProgram.channelName(),
                    createSchedule(scheduledPrograms.stream()
                            .filter(channelScheduledProgram -> channelScheduledProgram.channelName().equals(scheduledProgram.channelName()))
                            .toList(), days, startDate)
            ));
        }

        return schedule;
    }

    public static List<ScheduleGet> createSchedule(List<ChannelScheduledProgram> programs, Integer days, LocalDate start) {
        List<ScheduleGet> scheduledPrograms = new ArrayList<>();

        for (ChannelScheduledProgram program : programs) {
            List<RecurringDays> recurringDays = program.recurringDays();
            LocalTime startTime = program.startTime();
            LocalTime endTime = program.endTime();
            LocalDate programStartDate = program.startDate();
            LocalDate programEndDate = program.endDate();

            for (int i = 0; i < days; i++) {
                LocalDate airDate = start.plusDays(i);
                DayOfWeek dayOfWeek = airDate.getDayOfWeek();

                boolean addToSchedule = isProgramForAllDays(recurringDays, airDate, programStartDate, programEndDate)
                        || isProgramForSpecificDay(recurringDays, dayOfWeek, airDate, programStartDate, programEndDate)
                        || isProgramForOnce(recurringDays, airDate, programStartDate, programEndDate);

                if (addToSchedule) {
                    ScheduleGet scheduleProgram = new ScheduleGet(
                            dayOfWeek.toString(),
                            airDate,
                            program.programName(),
                            program.programDescription(),
                            program.programType(),
                            program.duration(),
                            startTime,
                            endTime
                    );

                    scheduledPrograms.add(scheduleProgram);
                }
            }
        }

        scheduledPrograms.sort(Comparator.comparing(ScheduleGet::airDate)
                .thenComparing(ScheduleGet::startTime));

        return scheduledPrograms;
    }

    public static void validateAvailableTimeSlotForNewProgram(ScheduleProgramCreate scheduleProgramCreate, List<ChannelScheduledProgram> scheduledPrograms, Program program) {
        LocalTime startTime = scheduleProgramCreate.startTime();
        LocalTime endTime = scheduleProgramCreate.startTime().plusMinutes(program.duration());
        LocalDate startDate = scheduleProgramCreate.startDate();

        for (ChannelScheduledProgram channelScheduledprogram : scheduledPrograms) {
            // Program scheduled for all days
            if (scheduleProgramCreate.recurringDays().contains(RecurringDays.ALL)) {
                // Can't conflict with any program at the time slot
                validateTimeSlot(startTime, endTime, channelScheduledprogram);
            }
            // Program scheduled for once
            else if (scheduleProgramCreate.recurringDays().contains(RecurringDays.NONE)) {
                // Can not schedule at the time slot of other program scheduled for all days
                if (channelScheduledprogram.recurringDays().contains(RecurringDays.ALL)) {
                    if (startDate.isBefore(channelScheduledprogram.startDate())) {
                        return;
                    }
                    validateTimeSlot(startTime, endTime, channelScheduledprogram);
                }
                // Can not schedule at the time slot of other program scheduled once
                if (channelScheduledprogram.recurringDays().contains(RecurringDays.NONE)) {  // Already scheduled program for once
                    if (startDate.equals(channelScheduledprogram.startDate())) {
                        validateTimeSlot(startTime, endTime, channelScheduledprogram);
                    }
                }
                // Can not schedule at the time slot of other program scheduled for specific days
                if (channelScheduledprogram.recurringDays().contains(RecurringDays.valueOf(startDate.getDayOfWeek().name()))) {
                    validateTimeSlot(startTime, endTime, channelScheduledprogram);
                }
            }
            // Program scheduled for specific days
            else {
                for (RecurringDays recurringDay : scheduleProgramCreate.recurringDays()) {
                    if (channelScheduledprogram.recurringDays().contains(RecurringDays.ALL) ||
                            getRecurringDay(recurringDay, channelScheduledprogram.recurringDays()) != null) {
                        validateTimeSlot(startTime, endTime, channelScheduledprogram);
                    }
                }
            }
        }
    }

    private static boolean isInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || !date.isBefore(startDate))
                && (endDate == null || !date.isAfter(endDate));
    }

    private static boolean isProgramForAllDays(List<RecurringDays> recurringDays, LocalDate airDate, LocalDate startDate, LocalDate endDate) {
        if (!recurringDays.contains(RecurringDays.ALL)) {
            return false;
        }

        return isInRange(airDate, startDate, endDate);
    }

    private static boolean isProgramForOnce(List<RecurringDays> recurringDays, LocalDate airDate, LocalDate startDate, LocalDate endDate) {
        if (!recurringDays.contains(RecurringDays.NONE)) {
            return false;
        }

        return startDate.equals(airDate) && endDate.equals(airDate);
    }

    private static boolean isProgramForSpecificDay(List<RecurringDays> recurringDays, DayOfWeek dayOfWeek, LocalDate airDate, LocalDate startDate, LocalDate endDate) {
        return recurringDays.contains(RecurringDays.valueOf(dayOfWeek.name()))
                && isInRange(airDate, startDate, endDate);
    }

    private static RecurringDays getRecurringDay(RecurringDays recurringDay, List<RecurringDays> recurringDays) {
        for (RecurringDays day : recurringDays) {
            if (day.equals(recurringDay)) {
                return day;
            }
        }

        return null;
    }

    private static void validateTimeSlot(LocalTime startTime, LocalTime endTime, ChannelScheduledProgram channelScheduledprogram) {
        boolean isSameStartTime = startTime.equals(channelScheduledprogram.startTime());
        boolean isStartTimeDuringOtherProgramAirTime = startTime.isAfter(channelScheduledprogram.startTime()) && startTime.isBefore(channelScheduledprogram.endTime());
        boolean isEndTimeDuringOtherProgramAirTime = endTime.isAfter(channelScheduledprogram.startTime()) && endTime.isBefore(channelScheduledprogram.endTime());

        if (isSameStartTime || isStartTimeDuringOtherProgramAirTime || isEndTimeDuringOtherProgramAirTime) {
            throw new ApiException("Cannot add program to schedule, conflicting with: " + channelScheduledprogram.programName(), HttpStatus.BAD_REQUEST);
        }
    }
}

