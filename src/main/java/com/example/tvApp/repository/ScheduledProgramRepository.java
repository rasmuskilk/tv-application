package com.example.tvApp.repository;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.mapper.rowMapper.ChannelScheduledProgramRowMapper;
import com.example.tvApp.model.ScheduledProgram;
import com.example.tvApp.model.dto.channelDTO.ChannelScheduledProgram;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ScheduledProgramRepository extends ListCrudRepository<ScheduledProgram, Integer> {
    @Query(value = """
                SELECT sp.end_time AS endTime, sp.start_time AS startTime, sp.recurring_days as recurringDays, sp.start_date as startDate, sp.end_date as endDate,
                c.name AS channelName,
                p.name AS programName, p.description AS programDescription, p.duration AS duration, p.number_of_episodes AS numberOfEpisodes, p.program_type AS programType
                FROM scheduled_programs sp
                LEFT JOIN channels c ON sp.channel_id = c.id
                LEFT JOIN programs p ON p.id = sp.program_id
                ORDER BY sp.start_time
            """, rowMapperClass = ChannelScheduledProgramRowMapper.class)
    List<ChannelScheduledProgram> findAllForChannels();

    @Query(value = """
                SELECT sp.end_time AS endTime, sp.start_time AS startTime, sp.recurring_days as recurringDays, sp.start_date as startDate, sp.end_date as endDate,
                c.name AS channelName,
                p.name AS programName, p.description AS programDescription, p.duration AS duration, p.number_of_episodes AS numberOfEpisodes, p.program_type AS programType
                FROM scheduled_programs sp
                LEFT JOIN channels c ON sp.channel_id = c.id
                LEFT JOIN programs p ON p.id = sp.program_id
                WHERE sp.channel_id = :channelId
                ORDER BY sp.start_time
            """, rowMapperClass = ChannelScheduledProgramRowMapper.class)
    List<ChannelScheduledProgram> findAllByChannelId(Integer channelId);

    @Query(value = """
                SELECT sp.end_time AS endTime, sp.start_time AS startTime, sp.recurring_days as recurringDays, sp.start_date as startDate, sp.end_date as endDate,
                c.name AS channelName,
                p.name AS programName, p.description AS programDescription, p.duration AS duration, p.number_of_episodes AS numberOfEpisodes, p.program_type AS programType
                FROM scheduled_programs sp
                LEFT JOIN channels c ON sp.channel_id = c.id
                LEFT JOIN programs p ON p.id = sp.program_id
                WHERE p.program_type = :programType
                ORDER BY sp.start_time
            """, rowMapperClass = ChannelScheduledProgramRowMapper.class)
    List<ChannelScheduledProgram> findAllByProgramType(ProgramType programType);

    @Modifying
    @Query(value = """
        DELETE FROM scheduled_programs WHERE channel_id = :channelId
            """)
    void deleteByChannelId(Integer channelId);

    @Modifying
    @Query(value = """
        DELETE FROM scheduled_programs WHERE program_id = :programId
            """)
    void deleteByProgramId(Integer programId);
}
