package com.example.cavesofzircon.events;

import org.hexworks.cobalt.events.api.Event;

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
}
