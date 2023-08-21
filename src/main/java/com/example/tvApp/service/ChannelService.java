package com.example.tvApp.service;

import com.example.tvApp.exceptions.ApiException;
import com.example.tvApp.helpers.ValidationHelpers;
import com.example.tvApp.mapper.ChannelMapper;
import com.example.tvApp.model.Channel;
import com.example.tvApp.model.dto.channelDTO.ChannelCreate;
import com.example.tvApp.repository.ChannelRepository;
import com.example.tvApp.repository.ScheduledProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ScheduledProgramRepository scheduledProgramRepository;

    @Autowired
    public ChannelService(ChannelRepository channelRepository, ScheduledProgramRepository scheduledProgramRepository) {
        this.channelRepository = channelRepository;
        this.scheduledProgramRepository = scheduledProgramRepository;
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel getChannelById(Integer id) {
        return ValidationHelpers.validateEntityExistsAndReturnEntity(channelRepository.findById(id), "Channel");
    }

    public Channel createChannel(ChannelCreate channelCreate) {
        try {
            return channelRepository.save(ChannelMapper.mapChannelCreate(channelCreate));
        } catch (RuntimeException e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Channel updateChannel(Integer id, ChannelCreate channelUpdate) {
        Channel channel = ValidationHelpers.validateEntityExistsAndReturnEntity(channelRepository.findById(id), "Channel");

        String nameToUpdate = channelUpdate.name() != null ? channelUpdate.name() : channel.name();
        String descriptionToUpdate = channelUpdate.description() != null ? channelUpdate.description() : channel.description();

        return channelRepository.save(new Channel(
                channel.id(),
                nameToUpdate,
                descriptionToUpdate
        ));
    }

    public Channel deleteChannelById(Integer id) {
        Channel channel = ValidationHelpers.validateEntityExistsAndReturnEntity(channelRepository.findById(id), "Channel");

        // Delete scheduled programs for the channel
        scheduledProgramRepository.deleteByChannelId(id);
        channelRepository.deleteById(id);

        return channel;
    }
}
