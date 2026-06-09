package com.example.cavesofzircon.events;

import org.hexworks.cobalt.events.api.Event;

public class PlayerDied implements Event {

    private final String cause;
    private final Object emitter;

    public PlayerDied(String cause, Object emitter) {
        this.cause = cause;
        this.emitter = emitter;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public Object getEmitter() {
        return emitter;
    }
}
