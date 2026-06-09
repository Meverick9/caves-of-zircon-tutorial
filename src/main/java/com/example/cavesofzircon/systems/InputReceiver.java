package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.types.ItemHolder;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.*;
import com.example.cavesofzircon.view.dialog.HelpDialog;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import org.hexworks.amethyst.api.base.BaseBehavior;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyCode;

public class InputReceiver extends BaseBehavior<GameContext> {

    public static final InputReceiver INSTANCE = new InputReceiver();

    private static final Continuation<Object> SYNC_CONT = new Continuation<>() {
        @Override public kotlin.coroutines.CoroutineContext getContext() { return EmptyCoroutineContext.INSTANCE; }
        @Override public void resumeWith(Object result) {}
    };

    private InputReceiver() {
        super();
    }

    @Override
    public Object update(Entity<? extends EntityType, GameContext> entity, GameContext context,
                         Continuation<? super Boolean> continuation) {
        var uiEvent = context.getUiEvent();
        var player = context.getPlayer();
        var currentPos = EntityExtensions.entityPosition(player);

        if (uiEvent instanceof KeyboardEvent keyEvent) {
            var code = keyEvent.getCode();
            if (code == KeyCode.KEY_W) {
                player.receiveMessage(new MoveTo(context, (Entity<EntityType, GameContext>)(Object) player, currentPos.withRelativeY(-1)), SYNC_CONT);
            } else if (code == KeyCode.KEY_A) {
                player.receiveMessage(new MoveTo(context, (Entity<EntityType, GameContext>)(Object) player, currentPos.withRelativeX(-1)), SYNC_CONT);
            } else if (code == KeyCode.KEY_S) {
                player.receiveMessage(new MoveTo(context, (Entity<EntityType, GameContext>)(Object) player, currentPos.withRelativeY(1)), SYNC_CONT);
            } else if (code == KeyCode.KEY_D) {
                player.receiveMessage(new MoveTo(context, (Entity<EntityType, GameContext>)(Object) player, currentPos.withRelativeX(1)), SYNC_CONT);
            } else if (code == KeyCode.KEY_R) {
                player.receiveMessage(new MoveUp(context, (Entity<EntityType, GameContext>)(Object) player), SYNC_CONT);
            } else if (code == KeyCode.KEY_F) {
                player.receiveMessage(new MoveDown(context, (Entity<EntityType, GameContext>)(Object) player), SYNC_CONT);
            } else if (code == KeyCode.KEY_P) {
                player.receiveMessage(new PickItemUp(context, (Entity<ItemHolder, GameContext>)(Object) player, currentPos), SYNC_CONT);
            } else if (code == KeyCode.KEY_I) {
                player.receiveMessage(new InspectInventory(context, (Entity<ItemHolder, GameContext>)(Object) player, currentPos), SYNC_CONT);
            } else if (code == KeyCode.KEY_H) {
                context.getScreen().openModal(new HelpDialog(context.getScreen()));
            }
        }
        return true;
    }
}
