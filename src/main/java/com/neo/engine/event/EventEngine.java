package com.neo.engine.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by neowyp on 2016/3/21.
 */
@Slf4j
@Component
public class EventEngine {
    protected final EventBus eventBus;

    @Autowired
    private ApplicationContext applicationContext;

    public EventEngine() {
        this(Runtime.getRuntime().availableProcessors() + 1);
    }

    public EventEngine(Integer threadCount) {
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadCount));
    }

    @PostConstruct
    public void registerListeners() {
        Map<String, IEvenListener> listeners = applicationContext.getBeansOfType(IEvenListener.class);
        for(IEvenListener eventListener : listeners.values()) {
            eventBus.register(eventListener);
        }
    }

    /**
     * 发布事件
     */
    public void sendMsg(Object event) {
        log.info("send an eventMsg({})", event);
        eventBus.post(event);
    }
}
