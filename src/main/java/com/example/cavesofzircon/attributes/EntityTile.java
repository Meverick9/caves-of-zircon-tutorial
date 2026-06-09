package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.zircon.api.data.Tile;

public class EntityTile extends BaseAttribute {

    private final Tile tile;

    public EntityTile(Tile tile) {
        this.tile = tile;
    }

    public EntityTile() {
        this.tile = Tile.empty();
    }

    public Tile getTile() {
        return tile;
    }
}
