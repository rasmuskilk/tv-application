package com.example.tvApp.mapper;

import com.example.tvApp.model.Channel;
import com.example.tvApp.model.dto.channelDTO.ChannelCreate;

public class ChannelMapper {
    public static Channel mapChannelCreate(ChannelCreate channelCreate) {
        return new Channel(
                null,
                channelCreate.name(),
                channelCreate.description()
        );
    }
}
