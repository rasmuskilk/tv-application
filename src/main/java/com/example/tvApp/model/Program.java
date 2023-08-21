package com.example.tvApp.model;

import com.example.tvApp.helpers.enums.ProgramType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "programs")
public record Program(
        @Id Integer id,
        String name,
        String description,
        Integer duration,
        Integer numberOfEpisodes,
        ProgramType programType
) {
}
