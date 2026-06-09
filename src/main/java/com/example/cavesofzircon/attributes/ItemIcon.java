package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.zircon.api.data.GraphicalTile;

public class ItemIcon extends BaseAttribute {

    private final GraphicalTile iconTile;

    public ItemIcon(GraphicalTile iconTile) {
        this.iconTile = iconTile;
    }

    public GraphicalTile getIconTile() {
        return iconTile;
    }
}
