package com.example.cavesofzircon.events;

import org.hexworks.cobalt.events.api.Event;

public class PlayerGainedLevel implements Event {

    private final Object emitter;

    public PlayerGainedLevel(Object emitter) {
        this.emitter = emitter;
    }

    @Override
    public Object getEmitter() {
        return emitter;
    }
}
