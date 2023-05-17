package com.example.cloneproject15.config;

import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentrySupport {

    public SentrySupport() {
        Sentry.init("https://5ce4d33c66d84062a1aa8cb31a22db8f@o4505154821095424.ingest.sentry.io/4505154827714560");
    }

    public void logSimpleMessage(String msg) {
        EventBuilder eventBuilder = new EventBuilder()
                .withMessage(msg)
                .withLevel(Event.Level.ERROR);
//                .withLogger(SentrySupport.class.getName());
        Sentry.capture(eventBuilder);
    }
}