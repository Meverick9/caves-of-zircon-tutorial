package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.messages.Dig;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class Diggable extends BaseFacet<GameContext, Dig> {

    public static final Diggable INSTANCE = new Diggable();

    private Diggable() {
        super(JvmClassMappingKt.getKotlinClass(Dig.class));
    }

    @Override
    public Object receive(Dig message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var target = message.getTarget();
        context.getWorld().removeEntity(target);
        return Consumed.INSTANCE;
    }
}
