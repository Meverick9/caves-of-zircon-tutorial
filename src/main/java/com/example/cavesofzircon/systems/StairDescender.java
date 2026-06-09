package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.events.PlayerWonTheGame;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.MoveDown;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;
import org.hexworks.cobalt.events.internal.ApplicationScope;
import org.hexworks.zircon.internal.Zircon;

public class StairDescender extends BaseFacet<GameContext, MoveDown> {

    public static final StairDescender INSTANCE = new StairDescender();

    private StairDescender() {
        super(JvmClassMappingKt.getKotlinClass(MoveDown.class));
    }

    @Override
    public Object receive(MoveDown message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var source = message.getSource();
        var world = context.getWorld();
        var pos = EntityExtensions.entityPosition(source);

        world.fetchBlockAt(pos).map(block -> {
            boolean hasStairsDown = false;
            boolean hasExit = false;
            for (var entity : block.getEntities()) {
                if (entity.getType().equals(EntityTypes.StairsDownType.INSTANCE)) hasStairsDown = true;
                if (entity.getType().equals(EntityTypes.ExitType.INSTANCE)) hasExit = true;
            }

            if (hasStairsDown) {
                Functions.logGameEvent("You move down one level...", StairDescender.this);
                world.moveEntity(source, pos.withRelativeZ(-1));
                world.scrollOneDown();
            } else if (hasExit) {
                EntityExtensions.whenTypeIs(source, EntityTypes.PlayerType.class, playerEntity -> {
                    var counter = EntityExtensions.getZirconCounter(playerEntity);
                    Zircon.INSTANCE.getEventBus().publish(
                            new PlayerWonTheGame(counter.getZirconCount(), StairDescender.this),
                            ApplicationScope.INSTANCE
                    );
                });
            } else {
                Functions.logGameEvent("You search for a trapdoor, but you find nothing.", StairDescender.this);
            }
            return block;
        });
        return Consumed.INSTANCE;
    }
}
