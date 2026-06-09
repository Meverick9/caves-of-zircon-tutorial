package com.example.cavesofzircon.attributes.types;

import org.hexworks.amethyst.api.base.BaseEntityType;
import org.hexworks.cobalt.core.platform.factory.UUIDFactory;

public final class EntityTypes {

    private EntityTypes() {}

    public static final class WallType extends BaseEntityType {
        public static final WallType INSTANCE = new WallType();
        private WallType() { super("wall", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class PlayerType extends BaseEntityType
            implements Combatant, ItemHolder, EnergyUser, EquipmentHolder, ExperienceGainer, ZirconHolder {
        public static final PlayerType INSTANCE = new PlayerType();
        private PlayerType() { super("player", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class FungusType extends BaseEntityType implements Combatant {
        public static final FungusType INSTANCE = new FungusType();
        private FungusType() { super("fungus", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class StairsDownType extends BaseEntityType {
        public static final StairsDownType INSTANCE = new StairsDownType();
        private StairsDownType() { super("stairs down", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class StairsUpType extends BaseEntityType {
        public static final StairsUpType INSTANCE = new StairsUpType();
        private StairsUpType() { super("stairs up", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class FOWType extends BaseEntityType {
        public static final FOWType INSTANCE = new FOWType();
        private FOWType() { super("Fog of War", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class BatType extends BaseEntityType implements Combatant, ItemHolder {
        public static final BatType INSTANCE = new BatType();
        private BatType() { super("bat", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class ZirconType extends BaseEntityType implements Item {
        public static final ZirconType INSTANCE = new ZirconType();
        private ZirconType() { super("Zircon", "A small piece of Zircon. Its value is unfathomable.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class BatMeatType extends BaseEntityType implements Food {
        public static final BatMeatType INSTANCE = new BatMeatType();
        private BatMeatType() { super("Bat meat", "Stringy bat meat. It is edible, but not tasty.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class DaggerType extends BaseEntityType implements Weapon {
        public static final DaggerType INSTANCE = new DaggerType();
        private DaggerType() { super("Rusty Dagger", "A small, rusty dagger made of some metal alloy.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class SwordType extends BaseEntityType implements Weapon {
        public static final SwordType INSTANCE = new SwordType();
        private SwordType() { super("Iron Sword", "A shiny sword made of iron. It is a two-hand weapon", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class StaffType extends BaseEntityType implements Weapon {
        public static final StaffType INSTANCE = new StaffType();
        private StaffType() { super("Wooden Staff", "A wooden staff made of birch. It has seen some use", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class LightArmorType extends BaseEntityType implements Armor {
        public static final LightArmorType INSTANCE = new LightArmorType();
        private LightArmorType() { super("Leather Tunic", "A tunic made of rugged leather. It is very comfortable.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class MediumArmorType extends BaseEntityType implements Armor {
        public static final MediumArmorType INSTANCE = new MediumArmorType();
        private MediumArmorType() { super("Chainmail", "A sturdy chainmail armor made of interlocking iron chains.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class HeavyArmorType extends BaseEntityType implements Armor {
        public static final HeavyArmorType INSTANCE = new HeavyArmorType();
        private HeavyArmorType() { super("Platemail", "A heavy and shiny platemail armor made of bronze.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class ClubType extends BaseEntityType implements Weapon {
        public static final ClubType INSTANCE = new ClubType();
        private ClubType() { super("Club", "A wooden club. It doesn't give you an edge over your opponent (haha).", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class JacketType extends BaseEntityType implements Armor {
        public static final JacketType INSTANCE = new JacketType();
        private JacketType() { super("Leather jacket", "Dirty and rugged jacket made of leather.", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class ZombieType extends BaseEntityType implements Combatant, ItemHolder {
        public static final ZombieType INSTANCE = new ZombieType();
        private ZombieType() { super("zombie", "", UUIDFactory.INSTANCE.randomUUID()); }
    }

    public static final class ExitType extends BaseEntityType {
        public static final ExitType INSTANCE = new ExitType();
        private ExitType() { super("exit", "", UUIDFactory.INSTANCE.randomUUID()); }
    }
}
