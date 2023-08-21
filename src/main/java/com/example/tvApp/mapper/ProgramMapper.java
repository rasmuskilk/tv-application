package com.example.tvApp.mapper;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.dto.programDTO.ProgramCreate;

public class ProgramMapper {
    public static Program mapProgramCreate(ProgramCreate programCreate) {
        return new Program(
                null,
                programCreate.name(),
                programCreate.description(),
                programCreate.duration(),
                programCreate.numberOfEpisodes(),
                programCreate.programType()
        );
    }
}
