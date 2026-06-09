package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.events.PlayerDied;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.Destroy;
import com.example.cavesofzircon.messages.EntityDestroyed;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;
import org.hexworks.cobalt.events.internal.ApplicationScope;
import org.hexworks.zircon.internal.Zircon;

public class Destructible extends BaseFacet<GameContext, Destroy> {

    public static final Destructible INSTANCE = new Destructible();

    private Destructible() {
        super(JvmClassMappingKt.getKotlinClass(Destroy.class));
    }

    @Override
    public Object receive(Destroy message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var destroyer = message.getSource();
        var target = message.getTarget();
        var cause = message.getCause();

        context.getWorld().removeEntity(target);
        destroyer.receiveMessage(new EntityDestroyed(context, target, destroyer), continuation);

        if (EntityExtensions.isPlayer(target)) {
            Zircon.INSTANCE.getEventBus().publish(
                    new PlayerDied("You died " + cause, this),
                    ApplicationScope.INSTANCE
            );
        }
        Functions.logGameEvent(target.getName() + " dies " + cause + ".", this);
        return Consumed.INSTANCE;
    }
}
