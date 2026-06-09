package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;

public class NutritionalValue extends BaseAttribute {

    private final int energy;

    public NutritionalValue(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }
}
