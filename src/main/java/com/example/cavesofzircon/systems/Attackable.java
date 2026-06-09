package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.Attack;
import com.example.cavesofzircon.messages.Destroy;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Pass;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Attackable extends BaseFacet<GameContext, Attack> {

    public static final Attackable INSTANCE = new Attackable();

    private static final Continuation<Object> SYNC_CONT = new Continuation<>() {
        @Override public kotlin.coroutines.CoroutineContext getContext() { return EmptyCoroutineContext.INSTANCE; }
        @Override public void resumeWith(Object result) {}
    };

    private Attackable() {
        super(JvmClassMappingKt.getKotlinClass(Attack.class));
    }

    @Override
    public Object receive(Attack message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var attacker = message.getSource();
        Entity<EntityType, GameContext> target = (Entity<EntityType, GameContext>)(Object) message.getTarget();

        if (EntityExtensions.isPlayer(attacker) || EntityExtensions.isPlayer(target)) {
            int damage = Math.max(0, EntityExtensions.getAttackValue(attacker) - EntityExtensions.getDefenseValue(target));
            int finalDamage = (int)(Math.random() * damage) + 1;
            var stats = EntityExtensions.getCombatStats(target);
            stats.setHp(stats.getHp() - finalDamage);

            Functions.logGameEvent("The " + attacker.getName() + " hits the " + target.getName() + " for " + finalDamage + "!", this);

            if (EntityExtensions.hasNoHealthLeft(message.getTarget())) {
                target.receiveMessage(new Destroy(context, attacker, target, "after receiving a blow to the head"), SYNC_CONT);
            }
            return Consumed.INSTANCE;
        } else {
            return Pass.INSTANCE;
        }
    }
}
