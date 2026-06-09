package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.cobalt.core.api.UUID;
import org.hexworks.cobalt.datatypes.Maybe;
import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.world.GameContext;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends BaseAttribute {

    private final int size;
    private final List<Entity<? extends Item, GameContext>> currentItems = new ArrayList<>();

    public Inventory(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public List<Entity<? extends Item, GameContext>> getItems() {
        return List.copyOf(currentItems);
    }

    public boolean isEmpty() {
        return currentItems.isEmpty();
    }

    public boolean isFull() {
        return currentItems.size() >= size;
    }

    public Maybe<Entity<? extends Item, GameContext>> findItemBy(UUID id) {
        return Maybe.Companion.ofNullable(
                currentItems.stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null)
        );
    }

    public boolean addItem(Entity<? extends Item, GameContext> item) {
        if (!isFull()) {
            return currentItems.add(item);
        }
        return false;
    }

    public boolean removeItem(Entity<? extends Item, GameContext> item) {
        return currentItems.remove(item);
    }
}
