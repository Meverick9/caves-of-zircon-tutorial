package com.example.cavesofzircon.events;

import org.hexworks.cobalt.events.api.Event;

public class PlayerWonTheGame implements Event {

    private final int zircons;
    private final Object emitter;

    public PlayerWonTheGame(int zircons, Object emitter) {
        this.zircons = zircons;
        this.emitter = emitter;
    }

    public int getZircons() {
        return zircons;
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
