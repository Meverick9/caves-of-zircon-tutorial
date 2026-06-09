package com.example.cavesofzircon.world;

import com.example.cavesofzircon.attributes.types.EntityTypes;
import org.hexworks.amethyst.api.Context;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.UIEvent;

public class GameContext implements Context {

    private final World world;
    private final Screen screen;
    private final UIEvent uiEvent;
    private final Entity<EntityTypes.PlayerType, GameContext> player;

    public GameContext(World world, Screen screen, UIEvent uiEvent,
                       Entity<EntityTypes.PlayerType, GameContext> player) {
        this.world = world;
        this.screen = screen;
        this.uiEvent = uiEvent;
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public Screen getScreen() {
        return screen;
    }

    public UIEvent getUiEvent() {
        return uiEvent;
    }

    public Entity<EntityTypes.PlayerType, GameContext> getPlayer() {
        return player;
    }
}
