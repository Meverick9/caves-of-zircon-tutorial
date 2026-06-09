package com.example.cavesofzircon.builders;

import com.example.cavesofzircon.attributes.*;
import com.example.cavesofzircon.attributes.flags.BlockOccupier;
import com.example.cavesofzircon.attributes.types.*;
import com.example.cavesofzircon.messages.Attack;
import com.example.cavesofzircon.messages.Dig;
import com.example.cavesofzircon.systems.*;
import com.example.cavesofzircon.world.GameContext;
import kotlin.Unit;
import org.hexworks.amethyst.api.EntitiesKt;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.GraphicalTilesetResources;
import org.hexworks.zircon.api.data.Tile;

import java.util.Random;

public final class EntityFactory {

    private EntityFactory() {}

    private static final Random RANDOM = new Random();

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.PlayerType, GameContext> newPlayer() {
        return (Entity<EntityTypes.PlayerType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.PlayerType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new Vision(9),
                            new EntityPosition(),
                            BlockOccupier.INSTANCE,
                            CombatStats.create(100, 10, 5),
                            new EntityTile(GameTileRepository.PLAYER),
                            new EntityActions(
                                    kotlin.jvm.JvmClassMappingKt.getKotlinClass(Dig.class),
                                    kotlin.jvm.JvmClassMappingKt.getKotlinClass(Attack.class)
                            ),
                            new Inventory(10),
                            new EnergyLevel(1000, 1000),
                            new Equipment(newClub(), newJacket()),
                            new Experience(),
                            new ZirconCounter()
                    );
                    builder.behaviors(InputReceiver.INSTANCE, EnergyExpender.INSTANCE);
                    builder.facets(
                            Movable.INSTANCE,
                            CameraMover.INSTANCE,
                            StairClimber.INSTANCE,
                            StairDescender.INSTANCE,
                            Attackable.INSTANCE,
                            Destructible.INSTANCE,
                            ZirconGatherer.INSTANCE,
                            ItemPicker.INSTANCE,
                            InventoryInspector.INSTANCE,
                            ItemDropper.INSTANCE,
                            EnergyExpender.INSTANCE,
                            DigestiveSystem.INSTANCE,
                            ExperienceAccumulator.INSTANCE
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.WallType, GameContext> newWall() {
        return (Entity<EntityTypes.WallType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.WallType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new EntityPosition(),
                            BlockOccupier.INSTANCE,
                            new EntityTile(GameTileRepository.WALL),
                            VisionBlocker.INSTANCE
                    );
                    builder.facets(Diggable.INSTANCE);
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.FungusType, GameContext> newFungus(FungusSpread fungusSpread) {
        return (Entity<EntityTypes.FungusType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.FungusType.INSTANCE,
                builder -> {
                    builder.attributes(
                            BlockOccupier.INSTANCE,
                            new EntityPosition(),
                            new EntityTile(GameTileRepository.FUNGUS),
                            fungusSpread,
                            CombatStats.create(10, 0, 0)
                    );
                    builder.facets(Attackable.INSTANCE, Destructible.INSTANCE);
                    builder.behaviors(FungusGrowth.INSTANCE);
                    return Unit.INSTANCE;
                }
        );
    }

    public static Entity<EntityTypes.FungusType, GameContext> newFungus() {
        return newFungus(new FungusSpread());
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.StairsDownType, GameContext> newStairsDown() {
        return (Entity<EntityTypes.StairsDownType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.StairsDownType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new EntityTile(GameTileRepository.STAIRS_DOWN),
                            new EntityPosition()
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.StairsUpType, GameContext> newStairsUp() {
        return (Entity<EntityTypes.StairsUpType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.StairsUpType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new EntityTile(GameTileRepository.STAIRS_UP),
                            new EntityPosition()
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.FOWType, GameContext> newFogOfWar() {
        return (Entity<EntityTypes.FOWType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.FOWType.INSTANCE,
                builder -> {
                    builder.behaviors(FogOfWar.INSTANCE);
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.BatType, GameContext> newBat() {
        var inventory = new Inventory(1);
        inventory.addItem(newBatMeat());
        return (Entity<EntityTypes.BatType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.BatType.INSTANCE,
                builder -> {
                    builder.attributes(
                            BlockOccupier.INSTANCE,
                            new EntityPosition(),
                            new EntityTile(GameTileRepository.BAT),
                            CombatStats.create(5, 2, 1),
                            new EntityActions(
                                    kotlin.jvm.JvmClassMappingKt.getKotlinClass(Attack.class)
                            ),
                            inventory
                    );
                    builder.facets(Movable.INSTANCE, Attackable.INSTANCE, ItemDropper.INSTANCE, LootDropper.INSTANCE, Destructible.INSTANCE);
                    builder.behaviors(Wanderer.INSTANCE);
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.ZirconType, GameContext> newZircon() {
        return (Entity<EntityTypes.ZirconType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.ZirconType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(
                                    Tile.newBuilder()
                                            .withName("white gem")
                                            .withTileset(GraphicalTilesetResources.nethack16x16())
                                            .buildGraphicalTile()
                            ),
                            new EntityPosition(),
                            new EntityTile(GameTileRepository.ZIRCON)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.BatMeatType, GameContext> newBatMeat() {
        return (Entity<EntityTypes.BatMeatType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.BatMeatType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(
                                    Tile.newBuilder()
                                            .withName("Meatball")
                                            .withTileset(GraphicalTilesetResources.nethack16x16())
                                            .buildGraphicalTile()
                            ),
                            new NutritionalValue(750),
                            new EntityPosition(),
                            new EntityTile(GameTileRepository.BAT_MEAT)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.DaggerType, GameContext> newDagger() {
        return (Entity<EntityTypes.DaggerType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.DaggerType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Dagger")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile()),
                            new EntityPosition(),
                            new ItemCombatStats(4, 0, "Weapon"),
                            new EntityTile(GameTileRepository.DAGGER)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.SwordType, GameContext> newSword() {
        return (Entity<EntityTypes.SwordType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.SwordType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Short sword")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile()),
                            new EntityPosition(),
                            new ItemCombatStats(6, 0, "Weapon"),
                            new EntityTile(GameTileRepository.SWORD)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.StaffType, GameContext> newStaff() {
        return (Entity<EntityTypes.StaffType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.StaffType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(Tile.newBuilder()
                                    .withName("staff")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile()),
                            new EntityPosition(),
                            new ItemCombatStats(4, 2, "Weapon"),
                            new EntityTile(GameTileRepository.STAFF)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.LightArmorType, GameContext> newLightArmor() {
        return (Entity<EntityTypes.LightArmorType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.LightArmorType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Leather armor")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile()),
                            new EntityPosition(),
                            new ItemCombatStats(0, 2, "Armor"),
                            new EntityTile(GameTileRepository.LIGHT_ARMOR)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.MediumArmorType, GameContext> newMediumArmor() {
        return (Entity<EntityTypes.MediumArmorType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.MediumArmorType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Chain mail")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile()),
                            new EntityPosition(),
                            new ItemCombatStats(0, 3, "Armor"),
                            new EntityTile(GameTileRepository.MEDIUM_ARMOR)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.HeavyArmorType, GameContext> newHeavyArmor() {
        return (Entity<EntityTypes.HeavyArmorType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.HeavyArmorType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Plate mail")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile()),
                            new EntityPosition(),
                            new ItemCombatStats(0, 4, "Armor"),
                            new EntityTile(GameTileRepository.HEAVY_ARMOR)
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.ClubType, GameContext> newClub() {
        return (Entity<EntityTypes.ClubType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.ClubType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemCombatStats(0, 0, "Weapon"),
                            new EntityTile(GameTileRepository.CLUB),
                            new EntityPosition(),
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Club")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile())
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.JacketType, GameContext> newJacket() {
        return (Entity<EntityTypes.JacketType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.JacketType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new ItemCombatStats(0, 0, "Armor"),
                            new EntityTile(GameTileRepository.JACKET),
                            new EntityPosition(),
                            new ItemIcon(Tile.newBuilder()
                                    .withName("Leather jacket")
                                    .withTileset(GraphicalTilesetResources.nethack16x16())
                                    .buildGraphicalTile())
                    );
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<? extends Weapon, GameContext> newRandomWeapon() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> newDagger();
            case 1 -> newSword();
            default -> newStaff();
        };
    }

    @SuppressWarnings("unchecked")
    public static Entity<? extends Armor, GameContext> newRandomArmor() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> newLightArmor();
            case 1 -> newMediumArmor();
            default -> newHeavyArmor();
        };
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.ZombieType, GameContext> newZombie() {
        var inventory = new Inventory(2);
        inventory.addItem(newRandomWeapon());
        inventory.addItem(newRandomArmor());
        return (Entity<EntityTypes.ZombieType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.ZombieType.INSTANCE,
                builder -> {
                    builder.attributes(
                            BlockOccupier.INSTANCE,
                            new EntityPosition(),
                            new EntityTile(GameTileRepository.ZOMBIE),
                            new Vision(10),
                            CombatStats.create(25, 8, 4),
                            inventory,
                            new EntityActions(
                                    kotlin.jvm.JvmClassMappingKt.getKotlinClass(Attack.class)
                            )
                    );
                    builder.facets(Movable.INSTANCE, Attackable.INSTANCE, ItemDropper.INSTANCE, LootDropper.INSTANCE, Destructible.INSTANCE);
                    builder.behaviors(HunterSeeker.INSTANCE.or(Wanderer.INSTANCE));
                    return Unit.INSTANCE;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static Entity<EntityTypes.ExitType, GameContext> newExit() {
        return (Entity<EntityTypes.ExitType, GameContext>) EntitiesKt.newEntityOfType(
                EntityTypes.ExitType.INSTANCE,
                builder -> {
                    builder.attributes(
                            new EntityTile(GameTileRepository.EXIT),
                            new EntityPosition()
                    );
                    return Unit.INSTANCE;
                }
        );
    }
}
