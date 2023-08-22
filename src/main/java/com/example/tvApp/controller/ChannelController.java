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
    @ResponseStatus(HttpStatus.OK)
    public List<Channel> getAllChannels() {
        return channelService.getAllChannels();
    }

    @Operation(summary = "Get channel by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Success response",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class))
    )
    @GetMapping("channels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Channel getChannelById(@PathVariable Integer id) {
        return channelService.getChannelById(id);
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
    @ResponseStatus(HttpStatus.CREATED)
    public Channel createChannel(@RequestBody ChannelCreate channelCreate) {
        return channelService.createChannel(channelCreate);
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
    @ResponseStatus(HttpStatus.OK)
    public Channel updateChannel(@PathVariable Integer id, @RequestBody ChannelCreate channelUpdate) {
        return channelService.updateChannel(id, channelUpdate);
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
    @ResponseStatus(HttpStatus.OK)
    public Channel deleteChannelById(@PathVariable Integer id) {
        return channelService.deleteChannelById(id);
    }
}
