package com.example.tvApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "channels")
public record Channel(
        @Id Integer id,
        String name,
        String description
) {
}
