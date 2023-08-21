package com.example.tvApp.repository;

import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.model.Program;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends ListCrudRepository<Program, Integer> {
    List<Program> findAllByProgramType(ProgramType programType);
}
