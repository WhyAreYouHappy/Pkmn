package ru.mirea.pkmn;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {
    public Card() {
    }
    public Card(String name){
        this.name = name;
    }

    public static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return  "1. " + pokemonStage + '\n'+
                "2. " + name + '\n' +
                "3. " + hp + '\n'  +
                "4. " + pokemonType + '\n'  +
                "5. " + (evolvesFrom != null && evolvesFrom.getName() != null ? evolvesFrom.getName() : "-") + '\n' +
                "6. " + skills.stream().map(AttackSkill::toString).reduce((a, b) -> a + ", " + b).orElse("") + '\n'  +
                "7. " + weaknessType + '\n'  +
                "8. " + ((resistanceType != null) ? resistanceType : "-") + '\n'  +
                "9. " + retreatCost + '\n' +
                "10. " + gameSet + '\n' +
                "11. " + regulationMark + '\n'  +
                "12. " + pokemonOwner;
    }

    public Card(PokemonStage pokemonStage, String name, int hp, EnergyType pokemonType, Card evolvesFrom, List<AttackSkill> skills, EnergyType weaknessType, EnergyType resistanceType, String retreatCost, String gameSet, char regulationMark, Student pokemonOwner) {
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
        this.regulationMark = regulationMark;
        this.pokemonOwner = pokemonOwner;
    }

    public PokemonStage getPokemonStage() {
        return pokemonStage;
    }
    public String getName() {
        return name;
    }
    public int getHp() {
        return hp;
    }
    public EnergyType getPokemonType() {
        return pokemonType;
    }
    public Card getEvolvesFrom() {
        return evolvesFrom;
    }
    public List<AttackSkill> getSkills() {
        return skills;
    }
    public EnergyType getWeaknessType() {
        return weaknessType;
    }
    public EnergyType getResistanceType() {
        return resistanceType;
    }
    public String getRetreatCost() {
        return retreatCost;
    }
    public String getGameSet() {
        return gameSet;
    }
    public char getRegulationMark() {
        return regulationMark;
    }
    public Student getPokemonOwner() {
        return pokemonOwner;
    }

    public void setPokemonStage(PokemonStage pokemonStage) {
        this.pokemonStage = pokemonStage;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setPokemonType(EnergyType pokemonType) {
        this.pokemonType = pokemonType;
    }
    public void setEvolvesFrom(Card evolvesFrom) {
        this.evolvesFrom = evolvesFrom;
    }
    public void setSkills(List<AttackSkill> skills) {
        this.skills = skills;
    }
    public void setWeaknessType(EnergyType weaknessType) {
        this.weaknessType = weaknessType;
    }
    public void setResistanceType(EnergyType resistanceType) {
        this.resistanceType = resistanceType;
    }
    public void setRetreatCost(String retreatCost) {
        this.retreatCost = retreatCost;
    }
    public void setGameSet(String gameSet) {
        this.gameSet = gameSet;
    }
    public void setRegulationMark(char regulationMark) {
        this.regulationMark = regulationMark;
    }
    public void setPokemonOwner(Student pokemonOwner) {
        this.pokemonOwner = pokemonOwner;
    }
}
