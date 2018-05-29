package hr.cleancode.message.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import hr.cleancode.message.api.ChannelService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ChannelService.class, ChannelServiceImpl.class);
    }
}
