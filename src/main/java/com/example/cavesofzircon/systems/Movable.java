package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.MoveCamera;
import com.example.cavesofzircon.messages.MoveTo;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.MessageResponse;
import org.hexworks.amethyst.api.Pass;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class Movable extends BaseFacet<GameContext, MoveTo> {

    public static final Movable INSTANCE = new Movable();

    private Movable() {
        super(JvmClassMappingKt.getKotlinClass(MoveTo.class));
    }

    @Override
    public Object receiveMessage(MoveTo message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var entity = message.getSource();
        var position = message.getPosition();
        var world = context.getWorld();
        var previousPosition = EntityExtensions.entityPosition(entity);
        Response result = Pass.INSTANCE;

        var block = world.fetchBlockAtOrNull(position);
        if (block != null) {
            if (block.isOccupied()) {
                result = EntityExtensions.tryActionsOn(entity, context, block.getOccupier().get());
            } else {
                if (world.moveEntity(entity, position)) {
                    result = Consumed.INSTANCE;
                    if (entity.getType().equals(EntityTypes.PlayerType.INSTANCE)) {
                        result = new MessageResponse<>(new MoveCamera(context, entity, previousPosition));
                    }
                }
            }
        }
        return result;
    }
}
