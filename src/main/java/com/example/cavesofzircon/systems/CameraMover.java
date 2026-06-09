package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.MoveCamera;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class CameraMover extends BaseFacet<GameContext, MoveCamera> {

    public static final CameraMover INSTANCE = new CameraMover();

    private CameraMover() {
        super(JvmClassMappingKt.getKotlinClass(MoveCamera.class));
    }

    @Override
    public Object receiveMessage(MoveCamera message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var source = message.getSource();
        var previousPosition = message.getPreviousPosition();
        var world = context.getWorld();
        var currentPosition = EntityExtensions.entityPosition(source);

        // screenPos = currentPosition - visibleOffset
        var visibleOffset = world.getVisibleOffset();
        var screenPosX = currentPosition.getX() - visibleOffset.getX();
        var screenPosY = currentPosition.getY() - visibleOffset.getY();

        int halfHeight = world.getVisibleSize().getYLength() / 2;
        int halfWidth = world.getVisibleSize().getXLength() / 2;

        if (previousPosition.getY() > currentPosition.getY() && screenPosY < halfHeight) {
            world.scrollOneBackward();
        } else if (previousPosition.getY() < currentPosition.getY() && screenPosY > halfHeight) {
            world.scrollOneForward();
        } else if (previousPosition.getX() > currentPosition.getX() && screenPosX < halfWidth) {
            world.scrollOneLeft();
        } else if (previousPosition.getX() < currentPosition.getX() && screenPosX > halfWidth) {
            world.scrollOneRight();
        }

        return Consumed.INSTANCE;
    }
}
