package ru.mirea.pkmn;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {
    private static final long serialVersionUID = 1L;

    private PokemonStage pokemonStage;
    private String name;
    private int hp;
    private EnergyType pokemonType;
    private Card evolvesFrom;
    private List<AttackSkill> skills;
    private EnergyType weaknessType;
    private EnergyType resistanceType;
    private String retreatCost;
    private String gameSet;
    private char regulationMark;
    private Student pokemonOwner;
    public String number;

    public Card(PokemonStage pokemonStage, String name, int hp, EnergyType pokemonType, Card evolvesFrom,
                List<AttackSkill> skills, EnergyType weaknessType, EnergyType resistanceType, String retreatCost,
                String gameSet, String number, char regulationMark, Student pokemonOwner) {
        this.pokemonStage = pokemonStage;
        this.name = name;
        this.hp = hp;
        this.pokemonType = pokemonType;
        this.evolvesFrom = evolvesFrom;
        this.skills = skills;
        this.weaknessType = weaknessType;
        this.resistanceType = resistanceType;
        this.retreatCost = retreatCost;
        this.gameSet = gameSet;
        this.number = number;
        this.regulationMark = regulationMark;
        this.pokemonOwner = pokemonOwner;
    }

    public Card() {}

    public PokemonStage getPokemonStage() {
        return pokemonStage;
    }

    public void setPokemonStage(PokemonStage pokemonStage) {
        this.pokemonStage = pokemonStage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public EnergyType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(EnergyType pokemonType) {
        this.pokemonType = pokemonType;
    }

    public Card getEvolvesFrom() {
        return evolvesFrom;
    }

    public void setEvolvesFrom(Card evolvesFrom) {
        this.evolvesFrom = evolvesFrom;
    }

    public List<AttackSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<AttackSkill> skills) {
        this.skills = skills;
    }

    public EnergyType getWeaknessType() {
        return weaknessType;
    }

    public void setWeaknessType(EnergyType weaknessType) {
        this.weaknessType = weaknessType;
    }

    public EnergyType getResistanceType() {
        return resistanceType;
    }

    public void setResistanceType(EnergyType resistanceType) {
        this.resistanceType = resistanceType;
    }

    public String getRetreatCost() {
        return retreatCost;
    }

    public void setRetreatCost(String retreatCost) {
        this.retreatCost = retreatCost;
    }

    public String getGameSet() {
        return gameSet;
    }

    public void setGameSet(String gameSet) {
        this.gameSet = gameSet;
    }

    public char getRegulationMark() {
        return regulationMark;
    }

    public void setRegulationMark(char regulationMark) {
        this.regulationMark = regulationMark;
    }

    public Student getPokemonOwner() {
        return pokemonOwner;
    }

    public void setPokemonOwner(Student pokemonOwner) {
        this.pokemonOwner = pokemonOwner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Card\n")
                .append("pokemonStage: ").append(pokemonStage).append("\n")
                .append("name: ").append(name).append("\n")
                .append("hp: ").append(hp).append("\n")
                .append("pokemonType: ").append(pokemonType).append("\n")
                .append("skills: ").append(skills).append("\n")
                .append("weaknessType: ").append(weaknessType != null ? weaknessType : "-").append("\n")
                .append("resistanceType: ").append(resistanceType != null ? resistanceType : "-").append("\n")
                .append("retreatCost: ").append(retreatCost).append("\n")
                .append("gameSet: ").append(gameSet).append("\n")
                .append("number: ").append(number).append("\n")
                .append("regulationMark: ").append(regulationMark).append("\n")
                .append(pokemonOwner != null ? pokemonOwner + "\n" : "");

        if (evolvesFrom != null) {
            result.append("evolvesFrom").append(evolvesFrom.toString());
        } else {
            result.append("Эволюция отстутствует: (evolvesFrom = null)");
        }

        return result.toString();
    }
}