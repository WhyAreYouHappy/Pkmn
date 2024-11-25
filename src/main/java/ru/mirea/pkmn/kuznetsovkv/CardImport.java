package ru.mirea.pkmn.kuznetsovkv;

import ru.mirea.pkmn.*;


import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CardImport {
    private final String filePath;
    private static final long serialVersionUID = 1L;

    public CardImport(String filePath) {
        this.filePath = filePath;
    }

    public Card loadCard() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return parseCard(reader);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке карты: " + e.getMessage());
            return null;
        }
    }

    private Card parseCard(BufferedReader reader) throws IOException {
        Card card = new Card();

        card.setPokemonStage(parsePokemonStage(reader.readLine()));
        card.setName(reader.readLine());
        card.setHp(Integer.parseInt(reader.readLine().trim()));
        card.setPokemonType(parseEnergyType(reader.readLine()));
        card.setEvolvesFrom(parseEvolvesFrom(reader.readLine().trim()));

        card.setSkills(parseSkills(reader.readLine().split(",")));
        card.setWeaknessType(parseEnergyType(reader.readLine().trim()));
        card.setResistanceType(parseEnergyType(reader.readLine().trim()));
        card.setRetreatCost(reader.readLine());
        card.setGameSet(reader.readLine());
        card.setRegulationMark(reader.readLine().charAt(0));
        card.setPokemonOwner(parseOwner(reader.readLine()));
        card.setNumber(reader.readLine());
        return card;
    }

    private PokemonStage parsePokemonStage(String stage) {
        return PokemonStage.valueOf(stage.toUpperCase().trim());
    }

    private EnergyType parseEnergyType(String type) {
        return (type == null || type.equals("-") || type.isEmpty()) ? null : EnergyType.valueOf(type.toUpperCase().trim());
    }

    private Card parseEvolvesFrom(String evolvesFromFile) throws IOException {
        if (evolvesFromFile.equals("-")) {
            return null;
        }
        String evolveFilePath = "src\\main\\resources\\" + evolvesFromFile;
        try (BufferedReader reader = new BufferedReader(new FileReader(evolveFilePath))) {
            return parseCard(reader);
        }
    }

    private List<AttackSkill> parseSkills(String[] abilitiesData) {
        List<AttackSkill> skills = new ArrayList<>();
        for (String ability : abilitiesData) {
            String[] abilityParts = ability.split("/");
            skills.add(new AttackSkill(
                    abilityParts[1].trim(),
                    "",
                    abilityParts[0].trim(),
                    Integer.parseInt(abilityParts[2].trim())));
        }
        return skills;
    }

    private Student parseOwner(String ownerLine) {
        String[] parts = ownerLine.split("/");
        return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
    }

    public Card deserializeCard(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Card) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при десериализации карты: " + e.getMessage());
            return null;
        }
    }
}
