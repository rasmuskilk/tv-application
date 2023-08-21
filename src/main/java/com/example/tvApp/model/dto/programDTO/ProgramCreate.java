package com.example.tvApp.model.dto.programDTO;

import com.example.tvApp.helpers.enums.ProgramType;

public record ProgramCreate(
        String name,
        String description,
        Integer duration,
        Integer numberOfEpisodes,
        ProgramType programType
) {
}
