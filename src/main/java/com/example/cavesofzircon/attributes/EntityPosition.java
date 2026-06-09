package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.zircon.api.data.Position3D;

public class EntityPosition extends BaseAttribute {

    private Position3D position;

    public EntityPosition() {
        this.position = Position3D.unknown();
    }

    public EntityPosition(Position3D initialPosition) {
        this.position = initialPosition;
    }

    public Position3D getPosition() {
        return position;
    }

    public void setPosition(Position3D position) {
        this.position = position;
    }
}
