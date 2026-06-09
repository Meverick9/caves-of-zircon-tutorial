package com.example.cavesofzircon.events;

import org.hexworks.cobalt.events.api.Event;

import java.util.Collections;

public class GameLogEvent implements Event {

    private final String text;
    private final Object emitter;

    public GameLogEvent(String text, Object emitter) {
        this.text = text;
        this.emitter = emitter;
    }

    public String getText() {
        return text;
    }

    @Override
    public Object getEmitter() {
        return emitter;
    }

    @Override
    public String getKey() {
        return Event.DefaultImpls.getKey(this);
    }

    @Override
    public Iterable<Event> getTrace() {
        return Event.DefaultImpls.getTrace(this);
    }
}
