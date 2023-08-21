package com.example.tvApp.model.dto.scheduleDTO;

import java.util.List;

public record ScheduleForAllChannels (
        String channelName,
        List<ScheduleGet> schedule
) {
}
