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
}
