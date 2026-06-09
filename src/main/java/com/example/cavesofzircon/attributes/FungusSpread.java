package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.GameConfig;
import org.hexworks.amethyst.api.base.BaseAttribute;

public class FungusSpread extends BaseAttribute {

    private int spreadCount;
    private final int maximumSpread;

    public FungusSpread() {
        this(0, GameConfig.MAXIMUM_FUNGUS_SPREAD);
    }

    public FungusSpread(int spreadCount, int maximumSpread) {
        this.spreadCount = spreadCount;
        this.maximumSpread = maximumSpread;
    }

    public int getSpreadCount() {
        return spreadCount;
    }

    public void setSpreadCount(int spreadCount) {
        this.spreadCount = spreadCount;
    }

    public int getMaximumSpread() {
        return maximumSpread;
    }
}
