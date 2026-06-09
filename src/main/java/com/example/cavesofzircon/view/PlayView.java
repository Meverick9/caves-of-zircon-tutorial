package com.example.cavesofzircon.view;

import com.example.cavesofzircon.GameConfig;
import com.example.cavesofzircon.builders.GameTileRepository;
import com.example.cavesofzircon.events.GameLogEvent;
import com.example.cavesofzircon.events.PlayerDied;
import com.example.cavesofzircon.events.PlayerGainedLevel;
import com.example.cavesofzircon.events.PlayerWonTheGame;
import com.example.cavesofzircon.view.dialog.LevelUpDialog;
import com.example.cavesofzircon.view.fragment.PlayerStatsFragment;
import com.example.cavesofzircon.world.Game;
import com.example.cavesofzircon.world.GameBuilder;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.events.api.DisposeSubscription;
import org.hexworks.cobalt.events.api.KeepSubscription;
import org.hexworks.cobalt.events.internal.ApplicationScope;
import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.game.ProjectionMode;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.api.view.base.BaseView;
import org.hexworks.zircon.internal.Zircon;
import org.hexworks.zircon.internal.game.impl.GameAreaComponentRenderer;

public class PlayView extends BaseView {

    private final TileGrid grid;

    public PlayView(TileGrid grid) {
        this(grid, GameBuilder.create());
    }

    public PlayView(TileGrid grid, Game game) {
        this(grid, game, GameConfig.THEME);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PlayView(TileGrid grid, Game game, org.hexworks.zircon.api.component.ColorTheme theme) {
        super(grid, theme);
        this.grid = grid;

        var sidebar = Components.panel()
                .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
                .withDecorations(ComponentDecorations.box())
                .build();

        sidebar.addFragment(new PlayerStatsFragment(sidebar.getContentSize().getWidth(), game.getPlayer()));

        var logArea = Components.logArea()
                .withDecorations(ComponentDecorations.box())
                .withSize(GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
                .withAlignmentWithin(getScreen(), ComponentAlignment.BOTTOM_RIGHT)
                .build();

        var gameComponent = Components.panel()
                .withSize(game.getWorld().getVisibleSize().to2DSize())
                .withComponentRenderer(new GameAreaComponentRenderer<>(
                        game.getWorld(),
                        Properties.createPropertyFrom(ProjectionMode.TOP_DOWN, v -> Boolean.TRUE),
                        GameTileRepository.FLOOR
                ))
                .withAlignmentWithin(getScreen(), ComponentAlignment.TOP_RIGHT)
                .build();

        getScreen().addComponents(sidebar, logArea, gameComponent);

        getScreen().handleKeyboardEvents(KeyboardEventType.KEY_PRESSED, (event, phase) -> {
            game.getWorld().update(getScreen(), event, game);
            return Processed.INSTANCE;
        });

        var eventBus = Zircon.INSTANCE.getEventBus();

        eventBus.<GameLogEvent>subscribeTo(ApplicationScope.INSTANCE, GameLogEvent.class.getSimpleName(),
                event -> {
                    logArea.addParagraph(event.getText(), false, 10);
                    return KeepSubscription.INSTANCE;
                });

        eventBus.<PlayerGainedLevel>subscribeTo(ApplicationScope.INSTANCE, PlayerGainedLevel.class.getSimpleName(),
                event -> {
                    getScreen().openModal(new LevelUpDialog(getScreen(), game.getPlayer()));
                    return KeepSubscription.INSTANCE;
                });

        eventBus.<PlayerWonTheGame>subscribeTo(ApplicationScope.INSTANCE, PlayerWonTheGame.class.getSimpleName(),
                event -> {
                    replaceWith(new WinView(grid, event.getZircons()));
                    return DisposeSubscription.INSTANCE;
                });

        eventBus.<PlayerDied>subscribeTo(ApplicationScope.INSTANCE, PlayerDied.class.getSimpleName(),
                event -> {
                    replaceWith(new LoseView(grid, event.getCause()));
                    return DisposeSubscription.INSTANCE;
                });

        game.getWorld().update(
                getScreen(),
                new KeyboardEvent(
                        KeyboardEventType.KEY_TYPED,
                        "",
                        KeyCode.DEAD_GRAVE,
                        false, false, false, false
                ),
                game
        );
    }
}
