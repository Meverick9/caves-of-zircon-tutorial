package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.FungusSpread;
import com.example.cavesofzircon.builders.EntityFactory;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.base.BaseBehavior;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.zircon.api.data.Size3D;

public class FungusGrowth extends BaseBehavior<GameContext> {

    public static final FungusGrowth INSTANCE = new FungusGrowth();

    private FungusGrowth() {
        super(JvmClassMappingKt.getKotlinClass(FungusSpread.class));
    }

    @Override
    public Object update(Entity<EntityType, GameContext> entity, GameContext context, Continuation<? super Boolean> continuation) {
        var world = context.getWorld();
        var fungusSpread = EntityExtensions.tryToFindAttribute(entity, FungusSpread.class);
        int spreadCount = fungusSpread.getSpreadCount();
        int maxSpread = fungusSpread.getMaximumSpread();
        if (spreadCount < maxSpread && Math.random() < 0.015) {
            var entityPos = EntityExtensions.entityPosition(entity);
            world.findEmptyLocationWithin(
                    entityPos.withRelativeX(-1).withRelativeY(-1),
                    Size3D.create(3, 3, 0)
            ).map(emptyLocation -> {
                world.addEntity(EntityFactory.newFungus(fungusSpread), emptyLocation);
                fungusSpread.setSpreadCount(fungusSpread.getSpreadCount() + 1);
                return emptyLocation;
            });
            return true;
        }
        return false;
    }
}
