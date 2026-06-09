package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.MoveUp;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class StairClimber extends BaseFacet<GameContext, MoveUp> {

    public static final StairClimber INSTANCE = new StairClimber();

    private StairClimber() {
        super(JvmClassMappingKt.getKotlinClass(MoveUp.class));
    }

    @Override
    public Object receive(MoveUp message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var player = message.getSource();
        var world = context.getWorld();
        var playerPos = EntityExtensions.entityPosition(player);

        world.fetchBlockAt(playerPos).map(block -> {
            boolean hasStairsUp = false;
            for (var entity : block.getEntities()) {
                if (entity.getType().equals(EntityTypes.StairsUpType.INSTANCE)) {
                    hasStairsUp = true;
                    break;
                }
            }
            if (hasStairsUp) {
                Functions.logGameEvent("You move up one level...", StairClimber.this);
                world.moveEntity(player, playerPos.withRelativeZ(1));
                world.scrollOneUp();
            } else {
                Functions.logGameEvent("You jump up and try to reach the ceiling. You fail.", StairClimber.this);
            }
            return block;
        });
        return Consumed.INSTANCE;
    }
}
