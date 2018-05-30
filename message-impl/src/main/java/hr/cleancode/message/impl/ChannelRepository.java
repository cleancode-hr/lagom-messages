package hr.cleancode.message.impl;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import hr.cleancode.message.api.Channel;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class ChannelRepository {

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();

    public void save(Channel channel) {
        channels.put(channel.getId().toString(), channel);
    }

    public List<Channel> findAll() {
        return ImmutableList.copyOf(channels.values().stream().sorted(Comparator.comparing(Channel::getTitle)).collect(Collectors.toList()));
    }
}
