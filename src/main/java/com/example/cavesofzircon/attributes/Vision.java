package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;

public class Vision extends BaseAttribute {

    private int radius;

    public Vision(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
