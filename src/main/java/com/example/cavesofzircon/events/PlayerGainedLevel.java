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

    @Override
    public String getKey() {
        return org.hexworks.cobalt.events.api.Event.DefaultImpls.getKey(this);
    }

    @Override
    public Iterable<org.hexworks.cobalt.events.api.Event> getTrace() {
        return org.hexworks.cobalt.events.api.Event.DefaultImpls.getTrace(this);
    }
}
