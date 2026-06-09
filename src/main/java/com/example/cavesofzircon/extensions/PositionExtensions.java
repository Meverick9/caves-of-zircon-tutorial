package com.example.cavesofzircon.extensions;

import org.hexworks.zircon.api.data.Position3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PositionExtensions {

    private PositionExtensions() {}

    public static List<Position3D> sameLevelNeighborsShuffled(Position3D pos) {
        var neighbors = new ArrayList<Position3D>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                var neighbor = pos.withRelativeX(x).withRelativeY(y);
                if (!neighbor.equals(pos)) {
                    neighbors.add(neighbor);
                }
            }
        }
        Collections.shuffle(neighbors);
        return neighbors;
    }
}
