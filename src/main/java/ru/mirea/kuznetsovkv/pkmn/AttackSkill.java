package ru.mirea.kuznetsovkv.pkmn;

import java.io.Serializable;

public class AttackSkill implements Serializable {
    @Override
    public String toString() {
        return cost + " / " + name + " / " + damage;
    }

    public static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String cost;
    private int damage;

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getCost() {
        return cost;
    }
    public int getDamage() {
        return damage;
    }

    public AttackSkill(String name, String description, String cost, int damage){
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.damage = damage;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
}
