package com.example.tvApp.mapper.rowMapper;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.helpers.enums.RecurringDays;
import com.example.tvApp.model.dto.channelDTO.ChannelScheduledProgram;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ChannelScheduledProgramRowMapper implements RowMapper<ChannelScheduledProgram> {
    @Override
    public ChannelScheduledProgram mapRow(ResultSet rs, int rowNum) throws SQLException {

        String recurringDaysJson = rs.getString("recurringDays");

        ObjectMapper objectMapper = new ObjectMapper();

        List<RecurringDays> recurringDaysList;
        try {
            recurringDaysList = objectMapper.readValue(recurringDaysJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new ChannelScheduledProgram(
                rs.getString("channelName"),
                rs.getString("programName"),
                rs.getString("programDescription"),
                rs.getInt("duration"),
                rs.getInt("numberOfEpisodes"),
                recurringDaysList,
                ProgramType.valueOf(rs.getString("programType")),
                rs.getObject("startTime", LocalTime.class),
                rs.getObject("endTime", LocalTime.class),
                rs.getObject("startDate", LocalDate.class),
                rs.getObject("endDate", LocalDate.class)
        );
    }
}
