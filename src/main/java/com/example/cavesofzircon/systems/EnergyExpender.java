package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.EnergyLevel;
import com.example.cavesofzircon.attributes.types.EnergyUser;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.Destroy;
import com.example.cavesofzircon.messages.Expend;
import com.example.cavesofzircon.world.GameContext;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseActor;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EnergyExpender extends BaseActor<GameContext, Expend> {

    public static final EnergyExpender INSTANCE = new EnergyExpender();

    private static final Continuation<Object> SYNC_CONT = new Continuation<>() {
        @Override public kotlin.coroutines.CoroutineContext getContext() { return EmptyCoroutineContext.INSTANCE; }
        @Override public void resumeWith(Object result) {}
    };

    private EnergyExpender() {
        super(JvmClassMappingKt.getKotlinClass(Expend.class),
              JvmClassMappingKt.getKotlinClass(EnergyLevel.class));
    }

    @Override
    public Object receive(Expend message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var entity = message.getSource();
        int energy = message.getEnergy();

        var energyLevel = EntityExtensions.tryToFindAttribute(entity, EnergyLevel.class);
        energyLevel.setCurrentEnergy(energyLevel.getCurrentEnergy() - energy);
        if (energyLevel.getCurrentEnergy() <= 0) {
            entity.receiveMessage(new Destroy(context, entity, entity, "because of starvation"), SYNC_CONT);
        }
        return Consumed.INSTANCE;
    }

    @Override
    public Object update(Entity<? extends EntityType, GameContext> entity, GameContext context,
                         Continuation<? super Boolean> continuation) {
        EntityExtensions.whenTypeIs(entity, EnergyUser.class, eu -> {
            @SuppressWarnings("unchecked")
            var energyUser = (Entity<EnergyUser, GameContext>)(Object) eu;
            energyUser.receiveMessage(new Expend(context, energyUser, 2), SYNC_CONT);
        });
        return true;
    }
}
