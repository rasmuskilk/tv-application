package com.example.tvApp.controller;

import com.example.tvApp.exceptions.ErrorResponse;
import com.example.tvApp.model.Channel;
import com.example.tvApp.model.dto.channelDTO.ChannelCreate;
import com.example.tvApp.service.ChannelService;
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

@Tag(name = "Channels")
@RestController
@RequestMapping("/api/v1")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Operation(summary = "Get all channels")
    @ApiResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Channel.class)))
    )
    @GetMapping("channels")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return new ResponseEntity<>(channelService.getAllChannels(), HttpStatus.OK);
    }

    @Operation(summary = "Get channel by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class))
    )
    @GetMapping("channels/{id}")
    public ResponseEntity<Channel> getChannelById(@PathVariable Integer id) {
        return new ResponseEntity<>(channelService.getChannelById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create new channel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Success response, channel created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class))
            )
    })
    @PostMapping("channels")
    public ResponseEntity<Channel> createChannel(@RequestBody ChannelCreate channelCreate) {
        return new ResponseEntity<>(channelService.createChannel(channelCreate), HttpStatus.CREATED);
    }

    @Operation(summary = "Update existing channel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response, channel updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/channels/{id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable Integer id, @RequestBody ChannelCreate channelUpdate) {
        return new ResponseEntity<>(channelService.updateChannel(id, channelUpdate), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete existing channel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response, channel deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("channels/{id}")
    public ResponseEntity<Channel> deleteChannelById(@PathVariable Integer id) {
        return new ResponseEntity<>(channelService.deleteChannelById(id), HttpStatus.OK);
    }
}
