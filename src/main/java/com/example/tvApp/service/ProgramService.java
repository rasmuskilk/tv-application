package com.example.tvApp.service;

import com.example.tvApp.exceptions.ApiException;
import com.example.tvApp.helpers.ValidationHelpers;
import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.mapper.ProgramMapper;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.dto.programDTO.ProgramCreate;
import com.example.tvApp.repository.ProgramRepository;
import com.example.tvApp.repository.ScheduledProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final ScheduledProgramRepository scheduledProgramRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository, ScheduledProgramRepository scheduledProgramRepository) {
        this.programRepository = programRepository;
        this.scheduledProgramRepository = scheduledProgramRepository;
    }

    public List<Program> getPrograms() {
        return programRepository.findAll();
    }

    public Program getProgramById(Integer id) {
        return ValidationHelpers.validateEntityExistsAndReturnEntity(programRepository.findById(id), "Program");
    }

    public List<Program> getByProgramType(ProgramType programType) {
        return programRepository.findAllByProgramType(programType);
    }

    public Program createProgram(ProgramCreate programCreate) {
        try {
            return programRepository.save(ProgramMapper.mapProgramCreate(programCreate));
        } catch (RuntimeException e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Program updateProgram(Integer id, ProgramCreate programUpdate) {
        Program program = ValidationHelpers.validateEntityExistsAndReturnEntity(programRepository.findById(id), "Program");

        String name = programUpdate.name() != null ? programUpdate.name() : program.name();
        String description = programUpdate.description() != null ? programUpdate.description() : program.description();
        Integer duration = programUpdate.duration() != null ? programUpdate.duration() : program.duration();
        Integer numberOfEpisodes = programUpdate.numberOfEpisodes() != null ? programUpdate.numberOfEpisodes() : program.numberOfEpisodes();
        ProgramType programType = programUpdate.programType() != null ? programUpdate.programType() : program.programType();

        return programRepository.save(new Program(
                program.id(),
                name,
                description,
                duration,
                numberOfEpisodes,
                programType
        ));
    }

    public Program deleteProgramById(Integer id) {
        Program program = ValidationHelpers.validateEntityExistsAndReturnEntity(programRepository.findById(id), "Program");

        // Delete all the scheduled programs
        scheduledProgramRepository.deleteByProgramId(id);
        programRepository.deleteById(id);

        return program;
    }
}
