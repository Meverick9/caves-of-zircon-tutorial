package com.example.cavesofzircon.functions;

import com.example.cavesofzircon.events.GameLogEvent;
import org.hexworks.cobalt.events.internal.ApplicationScope;
import org.hexworks.zircon.internal.Zircon;

public final class Functions {

    private Functions() {}

    public static void logGameEvent(String text, Object emitter) {
        Zircon.INSTANCE.getEventBus().publish(
                new GameLogEvent(text, emitter),
                ApplicationScope.INSTANCE
        );
    }
}
