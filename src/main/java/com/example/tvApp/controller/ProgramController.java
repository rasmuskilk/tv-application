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
import org.springframework.http.ResponseEntity;
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
    @GetMapping()
    public ResponseEntity<List<Program>> getPrograms() {
        return new ResponseEntity<>(programService.getPrograms(), HttpStatus.OK);
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
    public ResponseEntity<Program> getProgramById(@PathVariable Integer id) {
        return new ResponseEntity<>(programService.getProgramById(id), HttpStatus.OK);
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
    public ResponseEntity<List<Program>> getProgramsByProgramType(@PathVariable String programType) {
        ProgramType type = ValidationHelpers.validateAndReturnProgramType(programType);
        return new ResponseEntity<>(programService.getByProgramType(type), HttpStatus.OK);
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
    public ResponseEntity<Program> createProgram(@RequestBody ProgramCreate programCreate) {
        return new ResponseEntity<>(programService.createProgram(programCreate), HttpStatus.CREATED);
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
    public ResponseEntity<Program> updateProgram(@PathVariable Integer id, @RequestBody ProgramCreate programUpdate) {
        return new ResponseEntity<>(programService.updateProgram(id, programUpdate), HttpStatus.OK);
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
    public ResponseEntity<Program> deleteProgramById(@PathVariable Integer id) {
        return new ResponseEntity<>(programService.deleteProgramById(id), HttpStatus.OK);
    }
}
