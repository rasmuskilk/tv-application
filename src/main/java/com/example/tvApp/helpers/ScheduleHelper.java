package com.example.tvApp.helpers;

import com.example.tvApp.exceptions.ApiException;
import com.example.tvApp.helpers.enums.RecurringDays;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleForChannel;
import com.example.tvApp.model.dto.programDTO.ScheduleProgramCreate;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleForAllChannels;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleGet;
import org.springframework.http.HttpStatus;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleHelper {
    public static List<ScheduleForAllChannels> createScheduleForAllChannels(List<ScheduleForChannel> scheduledPrograms, Integer days, LocalDate startDate) {
        List<ScheduleForAllChannels> schedule = new ArrayList<>();

        Map<String, List<ScheduleForChannel>> scheduleMap = scheduledPrograms.stream()
                .collect(Collectors.groupingBy(ScheduleForChannel::channelName));

        for (Map.Entry<String, List<ScheduleForChannel>> entry : scheduleMap.entrySet()) {
            schedule.add(new ScheduleForAllChannels(entry.getKey(), createSchedule(entry.getValue(), days, startDate)));
        }

        return schedule;
    }

    public static List<ScheduleGet> createSchedule(List<ScheduleForChannel> programs, Integer days, LocalDate start) {
        List<ScheduleGet> scheduledPrograms = new ArrayList<>();

        for (ScheduleForChannel program : programs) {
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

    public static void validateAvailableTimeSlotForNewProgram(ScheduleProgramCreate scheduleProgramCreate, List<ScheduleForChannel> scheduledPrograms, Program program) {
        LocalTime startTime = scheduleProgramCreate.startTime();
        LocalTime endTime = scheduleProgramCreate.startTime().plusMinutes(program.duration());
        LocalDate startDate = scheduleProgramCreate.startDate();

        for (ScheduleForChannel scheduleForChannelScheduledprogram : scheduledPrograms) {
            // Program scheduled for all days
            if (scheduleProgramCreate.recurringDays().contains(RecurringDays.ALL)) {
                // Can't conflict with any program at the time slot
                validateTimeSlot(startTime, endTime, scheduleForChannelScheduledprogram);
            }
            // Program scheduled for once
            else if (scheduleProgramCreate.recurringDays().contains(RecurringDays.NONE)) {
                // Can not schedule at the time slot of other program scheduled for all days
                if (scheduleForChannelScheduledprogram.recurringDays().contains(RecurringDays.ALL)) {
                    if (startDate.isBefore(scheduleForChannelScheduledprogram.startDate())) {
                        return;
                    }
                    validateTimeSlot(startTime, endTime, scheduleForChannelScheduledprogram);
                }
                // Can not schedule at the time slot of other program scheduled once
                if (scheduleForChannelScheduledprogram.recurringDays().contains(RecurringDays.NONE)) {  // Already scheduled program for once
                    if (startDate.equals(scheduleForChannelScheduledprogram.startDate())) {
                        validateTimeSlot(startTime, endTime, scheduleForChannelScheduledprogram);
                    }
                }
                // Can not schedule at the time slot of other program scheduled for specific days
                if (scheduleForChannelScheduledprogram.recurringDays().contains(RecurringDays.valueOf(startDate.getDayOfWeek().name()))) {
                    validateTimeSlot(startTime, endTime, scheduleForChannelScheduledprogram);
                }
            }
            // Program scheduled for specific days
            else {
                for (RecurringDays recurringDay : scheduleProgramCreate.recurringDays()) {
                    if (scheduleForChannelScheduledprogram.recurringDays().contains(RecurringDays.ALL) ||
                            getRecurringDay(recurringDay, scheduleForChannelScheduledprogram.recurringDays()) != null) {
                        validateTimeSlot(startTime, endTime, scheduleForChannelScheduledprogram);
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

    private static void validateTimeSlot(LocalTime startTime, LocalTime endTime, ScheduleForChannel scheduleForChannelScheduledprogram) {
        boolean isSameStartTime = startTime.equals(scheduleForChannelScheduledprogram.startTime());
        boolean isStartTimeDuringOtherProgramAirTime = startTime.isAfter(scheduleForChannelScheduledprogram.startTime()) && startTime.isBefore(scheduleForChannelScheduledprogram.endTime());
        boolean isEndTimeDuringOtherProgramAirTime = endTime.isAfter(scheduleForChannelScheduledprogram.startTime()) && endTime.isBefore(scheduleForChannelScheduledprogram.endTime());

        if (isSameStartTime || isStartTimeDuringOtherProgramAirTime || isEndTimeDuringOtherProgramAirTime) {
            throw new ApiException("Cannot add program to schedule, conflicting with: " + scheduleForChannelScheduledprogram.programName(), HttpStatus.BAD_REQUEST);
        }
    }
}

