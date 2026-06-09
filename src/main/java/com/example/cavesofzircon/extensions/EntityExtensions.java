package com.example.cavesofzircon.extensions;

import com.example.cavesofzircon.attributes.*;
import com.example.cavesofzircon.attributes.flags.BlockOccupier;
import com.example.cavesofzircon.attributes.types.*;
import com.example.cavesofzircon.messages.EntityAction;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Pass;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.zircon.api.data.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EntityExtensions {

    private EntityExtensions() {}

    private static final Continuation<Object> SYNC_CONT = new Continuation<>() {
        @Override public kotlin.coroutines.CoroutineContext getContext() { return EmptyCoroutineContext.INSTANCE; }
        @Override public void resumeWith(Object result) { /* synchronous - should not be called */ }
    };

    public static com.example.cavesofzircon.world.GameContext getPosition(Entity<?, GameContext> entity) {
        throw new UnsupportedOperationException("Use getEntityPosition instead");
    }

    public static org.hexworks.zircon.api.data.Position3D getPosition(Entity<?, GameContext> entity,
                                                                        Void unused) {
        return tryToFindAttribute(entity, EntityPosition.class).getPosition();
    }

    public static org.hexworks.zircon.api.data.Position3D entityPosition(Entity<?, GameContext> entity) {
        return tryToFindAttribute(entity, EntityPosition.class).getPosition();
    }

    public static void setPosition(Entity<?, GameContext> entity,
                                   org.hexworks.zircon.api.data.Position3D position) {
        entity.findAttribute(JvmClassMappingKt.getKotlinClass(EntityPosition.class))
                .ifPresent(ep -> ep.setPosition(position));
    }

    public static boolean blocksVision(Entity<?, GameContext> entity) {
        return entity.findAttribute(JvmClassMappingKt.getKotlinClass(VisionBlocker.class)).isPresent();
    }

    public static boolean isPlayer(Entity<?, GameContext> entity) {
        return entity.getType().equals(EntityTypes.PlayerType.INSTANCE);
    }

    public static Tile getTile(Entity<?, GameContext> entity) {
        return tryToFindAttribute(entity, EntityTile.class).getTile();
    }

    public static boolean occupiesBlock(Entity<?, GameContext> entity) {
        return entity.findAttribute(JvmClassMappingKt.getKotlinClass(BlockOccupier.class)).isPresent();
    }

    public static boolean hasNoHealthLeft(Entity<? extends Combatant, GameContext> entity) {
        return getCombatStats(entity).getHp() <= 0;
    }

    public static CombatStats getCombatStats(Entity<?, GameContext> entity) {
        return tryToFindAttribute(entity, CombatStats.class);
    }

    public static <T extends org.hexworks.amethyst.api.Attribute> T tryToFindAttribute(
            Entity<?, GameContext> entity, Class<T> klass) {
        return entity.findAttribute(JvmClassMappingKt.getKotlinClass(klass))
                .orElseThrow(() -> new NoSuchElementException(
                        "Entity '" + entity + "' has no property with type '" + klass.getSimpleName() + "'."));
    }

    public static int getAttackValue(Entity<?, GameContext> entity) {
        int combat = entity.findAttribute(JvmClassMappingKt.getKotlinClass(CombatStats.class))
                .map(CombatStats::getAttackValue).orElse(0);
        int equip = entity.findAttribute(JvmClassMappingKt.getKotlinClass(Equipment.class))
                .map(Equipment::getAttackValue).orElse(0);
        int item = entity.findAttribute(JvmClassMappingKt.getKotlinClass(ItemCombatStats.class))
                .map(ItemCombatStats::getAttackValue).orElse(0);
        return combat + equip + item;
    }

    public static int getDefenseValue(Entity<?, GameContext> entity) {
        int combat = entity.findAttribute(JvmClassMappingKt.getKotlinClass(CombatStats.class))
                .map(CombatStats::getDefenseValue).orElse(0);
        int equip = entity.findAttribute(JvmClassMappingKt.getKotlinClass(Equipment.class))
                .map(Equipment::getDefenseValue).orElse(0);
        int item = entity.findAttribute(JvmClassMappingKt.getKotlinClass(ItemCombatStats.class))
                .map(ItemCombatStats::getDefenseValue).orElse(0);
        return combat + equip + item;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityType> void whenTypeIs(
            Entity<?, GameContext> entity, Class<T> typeClass, Consumer<Entity<T, GameContext>> fn) {
        if (typeClass.isAssignableFrom(entity.getType().getClass())) {
            fn.accept((Entity<T, GameContext>) entity);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityType> List<Entity<T, GameContext>> filterType(
            Iterable<Entity<?, GameContext>> entities, Class<T> typeClass) {
        var result = new ArrayList<Entity<T, GameContext>>();
        for (var entity : entities) {
            if (typeClass.isAssignableFrom(entity.getType().getClass())) {
                result.add((Entity<T, GameContext>) entity);
            }
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Response tryActionsOn(Entity<?, GameContext> entity,
                                         GameContext context,
                                         Entity<?, GameContext> target) {
        Response result = Pass.INSTANCE;
        var ea = entity.findAttributeOrNull(JvmClassMappingKt.getKotlinClass(EntityActions.class));
        if (ea != null) {
            for (var action : ea.createActionsFor(context,
                    (Entity<EntityType, GameContext>) entity,
                    (Entity<EntityType, GameContext>) target)) {
                Object msgResult = target.receiveMessage((org.hexworks.amethyst.api.Message) action, SYNC_CONT);
                if (msgResult instanceof Consumed) {
                    result = Consumed.INSTANCE;
                    break;
                }
            }
        }
        return result;
    }

    public static Inventory getInventory(Entity<? extends ItemHolder, GameContext> entity) {
        return tryToFindAttribute(entity, Inventory.class);
    }

    public static boolean addItem(Entity<? extends ItemHolder, GameContext> entity,
                                   Entity<? extends Item, GameContext> item) {
        return getInventory(entity).addItem(item);
    }

    public static boolean removeItem(Entity<? extends ItemHolder, GameContext> entity,
                                      Entity<? extends Item, GameContext> item) {
        return getInventory(entity).removeItem(item);
    }

    public static Equipment getEquipment(Entity<? extends EquipmentHolder, GameContext> entity) {
        return tryToFindAttribute(entity, Equipment.class);
    }

    @SuppressWarnings("unchecked")
    public static Entity<? extends CombatItem, GameContext> equip(
            Entity<? extends EquipmentHolder, GameContext> entity,
            Inventory inventory,
            Entity<? extends CombatItem, GameContext> item) {
        return getEquipment(entity).equip(inventory, item);
    }

    public static EnergyLevel getEnergyLevel(Entity<? extends EnergyUser, GameContext> entity) {
        return tryToFindAttribute(entity, EnergyLevel.class);
    }

    public static Experience getExperience(Entity<? extends ExperienceGainer, GameContext> entity) {
        return tryToFindAttribute(entity, Experience.class);
    }

    public static CombatStats getExperienceGainerCombatStats(Entity<? extends ExperienceGainer, GameContext> entity) {
        return tryToFindAttribute(entity, CombatStats.class);
    }

    public static ZirconCounter getZirconCounter(Entity<? extends ZirconHolder, GameContext> entity) {
        return tryToFindAttribute(entity, ZirconCounter.class);
    }

    public static int getFoodEnergy(Entity<? extends Food, GameContext> entity) {
        return tryToFindAttribute(entity, NutritionalValue.class).getEnergy();
    }

    public static Tile getItemTile(Entity<? extends Item, GameContext> entity) {
        return tryToFindAttribute(entity, EntityTile.class).getTile();
    }

    public static org.hexworks.zircon.api.data.GraphicalTile getItemIconTile(Entity<? extends Item, GameContext> entity) {
        return tryToFindAttribute(entity, ItemIcon.class).getIconTile();
    }

    public static int getItemCombatAttack(Entity<? extends Armor, GameContext> entity) {
        return tryToFindAttribute(entity, ItemCombatStats.class).getAttackValue();
    }

    public static int getItemCombatDefense(Entity<? extends Armor, GameContext> entity) {
        return tryToFindAttribute(entity, ItemCombatStats.class).getDefenseValue();
    }

    public static int getWeaponAttackValue(Entity<? extends Weapon, GameContext> entity) {
        return tryToFindAttribute(entity, ItemCombatStats.class).getAttackValue();
    }

    public static int getWeaponDefenseValue(Entity<? extends Weapon, GameContext> entity) {
        return tryToFindAttribute(entity, ItemCombatStats.class).getDefenseValue();
    }
}
