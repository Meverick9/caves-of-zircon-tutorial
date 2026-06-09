package com.example.cavesofzircon.view.fragment;

import com.example.cavesofzircon.attributes.DisplayableAttribute;
import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;

public class PlayerStatsFragment implements Fragment {

    private final Component root;

    public PlayerStatsFragment(int width, Entity<EntityTypes.PlayerType, GameContext> player) {
        var vbox = Components.vbox()
                .withSize(width, 30)
                .withSpacing(1)
                .build();

        vbox.addComponent(Components.header().withText("Player"));

        var attrIter = player.getAttributes().iterator();
        while (attrIter.hasNext()) {
            var attribute = attrIter.next();
            if (attribute instanceof DisplayableAttribute da) {
                vbox.addComponent(da.toComponent(width));
            }
        }

        this.root = vbox;
    }

    @Override
    public Component getRoot() {
        return root;
    }
}
