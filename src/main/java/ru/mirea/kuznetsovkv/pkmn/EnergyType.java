package ru.mirea.kuznetsovkv.pkmn;

public enum EnergyType {
    FIRE("FIRE"),
    GRASS("GRASS"),
    WATER("WATER"),
    LIGHTNING("LIGHTNING"),
    PSYCHIC("PSYCHIC"),
    FIGHTING("FIGHTING"),
    DARKNESS("DARKNESS"),
    METAL("METAL"),
    FAIRY("FAIRY"),
    DRAGON("DRAGON"),
    COLORLESS("COLORLESS");

    private String type;

    EnergyType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
