package com.example.tvApp.helpers;

import com.example.tvApp.exceptions.ApiException;
import com.example.tvApp.helpers.enums.ProgramType;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

public class ValidationHelpers {
    public static <T> void validateEntityExists(Optional<T> entity, String entityName) {
        if (entity.isEmpty()) {
            throw new ApiException(entityName + " not found", HttpStatus.NOT_FOUND);
        }
    }

    public static <T> T validateEntityExistsAndReturnEntity(Optional<T> entity, String entityName) {
        if (entity.isEmpty()) {
            throw new ApiException(entityName + " not found", HttpStatus.NOT_FOUND);
        }

        return entity.get();
    }

    public static void validateStartAndEndDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ApiException("Start date must be before end date", HttpStatus.BAD_REQUEST);
        }
    }

    public static ProgramType validateAndReturnProgramType(String programType) {
        try {
            return ProgramType.valueOf(programType.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = "Unknown program type: '" + programType + "'. Expected program types: " + Arrays.toString(ProgramType.values());
            throw new ApiException(message, HttpStatus.BAD_REQUEST);
        }
    }
}
