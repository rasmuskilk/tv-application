package com.example.tvApp.repository;

import com.example.tvApp.model.Channel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends ListCrudRepository<Channel, Integer> {
}
