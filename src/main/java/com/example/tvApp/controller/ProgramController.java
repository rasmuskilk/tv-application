package com.example.tvApp.controller;

import com.example.tvApp.exceptions.ErrorResponse;
import com.example.tvApp.helpers.ValidationHelpers;
import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.model.Program;
import com.example.tvApp.model.dto.programDTO.ProgramCreate;
import com.example.tvApp.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Programs")
@RestController
@RequestMapping("/api/v1/programs")
public class ProgramController {
    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @Operation(summary = "Get all programs")
    @ApiResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Program.class)))
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<Program> getPrograms() {
        return programService.getPrograms();
    }

    @Operation(summary = "Get program by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response - program found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Program.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Program not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public Program getProgramById(@PathVariable Integer id) {
        return programService.getProgramById(id);
    }

    @Operation(summary = "Get all programs by program type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Program.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - unknown program type",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping("/type/{programType}")
    @ResponseStatus(HttpStatus.OK)
    public List<Program> getProgramsByProgramType(@PathVariable String programType) {
        ProgramType type = ValidationHelpers.validateAndReturnProgramType(programType);
        return programService.getByProgramType(type);
    }

    @Operation(summary = "Create new program")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Success response, program created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Program.class))
            )
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Program createProgram(@RequestBody ProgramCreate programCreate) {
        return programService.createProgram(programCreate);
    }

    @Operation(summary = "Update program")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response, program updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Program.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Program not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Program updateProgram(@PathVariable Integer id, @RequestBody ProgramCreate programUpdate) {
        return programService.updateProgram(id, programUpdate);
    }

    @Operation(summary = "Delete program")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response, program deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Program.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Program not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Program deleteProgramById(@PathVariable Integer id) {
        return programService.deleteProgramById(id);
    }
}
