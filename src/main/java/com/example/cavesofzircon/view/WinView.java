package com.example.cavesofzircon.view;

import com.example.cavesofzircon.GameConfig;
import com.example.cavesofzircon.world.GameBuilder;
import org.hexworks.zircon.api.ColorThemes;
import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.grid.TileGrid;
import kotlin.Unit;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.api.view.base.BaseView;

public class WinView extends BaseView {

    public WinView(TileGrid grid, int zircons) {
        super(grid, ColorThemes.arc());

        var header = Components.textBox(GameConfig.WINDOW_WIDTH / 2)
                .addHeader("You won!")
                .addNewLine()
                .addParagraph("Congratulations! You have escaped from Caves of Zircon!", false)
                .addParagraph("You've managed to find " + zircons + " Zircons.")
                .withAlignmentWithin(getScreen(), ComponentAlignment.CENTER)
                .build();

        var restartButton = Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_LEFT)
                .withText("Restart")
                .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
                .build();

        var exitButton = Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_RIGHT)
                .withText("Quit")
                .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
                .build();

        restartButton.onActivated(action -> {
            replaceWith(new PlayView(grid, new GameBuilder(GameConfig.WORLD_SIZE).buildGame()));
            return kotlin.Unit.INSTANCE;
        });

        exitButton.onActivated(action -> {
            System.exit(0);
            return kotlin.Unit.INSTANCE;
        });

        getScreen().addComponent(header);
        getScreen().addComponent(restartButton);
        getScreen().addComponent(exitButton);
    }
}
