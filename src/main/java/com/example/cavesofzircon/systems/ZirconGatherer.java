package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.ZirconCounter;
import com.example.cavesofzircon.attributes.types.ZirconHolder;
import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.PickItemUp;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Pass;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class ZirconGatherer extends BaseFacet<GameContext, PickItemUp> {

    public static final ZirconGatherer INSTANCE = new ZirconGatherer();

    private ZirconGatherer() {
        super(JvmClassMappingKt.getKotlinClass(PickItemUp.class),
              JvmClassMappingKt.getKotlinClass(ZirconCounter.class));
    }

    @Override
    public Object receive(PickItemUp message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var source = message.getSource();
        var position = message.getPosition();
        var world = context.getWorld();
        final Response[] response = {Pass.INSTANCE};

        world.findTopItem(position).map(item -> {
            EntityExtensions.whenTypeIs(source, ZirconHolder.class, zirconHolder -> {
                if (item.getType().equals(EntityTypes.ZirconType.INSTANCE)) {
                    var counter = EntityExtensions.getZirconCounter(zirconHolder);
                    counter.setZirconCount(counter.getZirconCount() + 1);
                    world.removeEntity((org.hexworks.amethyst.api.entity.Entity<org.hexworks.amethyst.api.entity.EntityType, com.example.cavesofzircon.world.GameContext>)(Object) item);
                    Functions.logGameEvent(zirconHolder.getName() + " picked up a Zircon!", ZirconGatherer.this);
                    response[0] = Consumed.INSTANCE;
                }
            });
            return item;
        });
        return response[0];
    }
}
