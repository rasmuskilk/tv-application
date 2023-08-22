package com.example.tvApp.controller;

import com.example.tvApp.exceptions.ErrorResponse;
import com.example.tvApp.helpers.ValidationHelpers;
import com.example.tvApp.helpers.enums.ProgramType;
import com.example.tvApp.model.dto.programDTO.ScheduleProgramCreate;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleForAllChannels;
import com.example.tvApp.model.dto.scheduleDTO.ScheduleGet;
import com.example.tvApp.model.dto.scheduleDTO.ScheduledProgramCreated;
import com.example.tvApp.service.ScheduledProgramService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Scheduled programs")
@RestController
@RequestMapping("/api/v1/schedule")
@Validated
public class ScheduledProgramController {
    private final ScheduledProgramService scheduledProgramService;

    @Autowired
    public ScheduledProgramController(ScheduledProgramService scheduledProgramService) {
        this.scheduledProgramService = scheduledProgramService;
    }

    @Operation(summary = "Get schedule for all the channels")
    @ApiResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleForAllChannels.class)))
    )
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleForAllChannels> getScheduleForAllChannels(
            @Parameter(description = "Format - YYYY-MM-DD")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Format - YYYY-MM-DD")
            @RequestParam(required = false) LocalDate endDate
    ) {
        return scheduledProgramService.getScheduleForAllChannels(startDate, endDate);
    }

    @Operation(summary = "Get schedule for specific channel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleGet.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/channel/{channelId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleGet> getScheduleByChannelId(
            @PathVariable Integer channelId,
            @Parameter(description = "Format - YYYY-MM-DD")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Format - YYYY-MM-DD")
            @RequestParam(required = false) LocalDate endDate
    ) {
        return scheduledProgramService.getScheduleByChannelId(channelId, startDate, endDate);
    }

    @Operation(summary = "Get schedule for programs with specific program type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleForAllChannels.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - unknown program type",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping("/type/{programType}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleForAllChannels> getScheduleByChannelIdAndByProgramType(
            @Parameter(description = "NEWS, MOVIE, DOCUMENTARY, ANIMATION, KIDS_SHOW, TV_SHOW")
            @PathVariable String programType,
            @Parameter(description = "Format - YYYY-MM-DD")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Format - YYYY-MM-DD")
            @RequestParam(required = false) LocalDate endDate
    ) {
        ProgramType type = ValidationHelpers.validateAndReturnProgramType(programType);

        return scheduledProgramService.getScheduleByProgramType(type, startDate, endDate);
    }

    @Operation(summary = "Add program to schedule")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response, program added to schedule",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleGet.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Program not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cold not add program to schedule, conflicting with other programs",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/channel/{channelId}/program")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduledProgramCreated addProgramToChannelByProgramId(
            @PathVariable Integer channelId,
            @RequestBody ScheduleProgramCreate programToSchedule) throws JsonProcessingException {
        return scheduledProgramService.addProgramToChannelScheduleByProgramId(channelId, programToSchedule);
    }

    @Operation(summary = "Remove program from schedule")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response, program added",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ScheduleGet.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Program not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @DeleteMapping("/channel/{channelId}/program/{programId}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProgramFromSchedule(
            @PathVariable Integer channelId,
            @PathVariable Integer programId) {

        return scheduledProgramService.removeProgramFromSchedule(channelId, programId);

    }
}
