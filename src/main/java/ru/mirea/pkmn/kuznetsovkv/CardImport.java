package ru.mirea.pkmn.kuznetsovkv;

import ru.mirea.pkmn.*;


import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CardImport {
    public ru.mirea.pkmn.Card importCard(String path) {

        Card card = new Card();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            card.setPokemonStage(PokemonStage.valueOf(br.readLine().split("\\. ")[1]));
            card.setName(br.readLine().split("\\. ")[1]);
            card.setHp(Integer.parseInt(br.readLine().split("\\. ")[1]));
            card.setPokemonType(EnergyType.valueOf(br.readLine().split("\\. ")[1]));
            String evolution = br.readLine().split("\\. ")[1];

            if (!evolution.equals("-")){
                card.setEvolvesFrom(new Card(evolution));
            }

            String[] skill = br.readLine().split("\\. ")[1].split(", ");
            List<AttackSkill> skills = new ArrayList<>();

            for (String i : skill){
                String[] arr = i.split(" / ");
                AttackSkill attackSkill = new AttackSkill(arr[1], "", arr[0], Integer.parseInt(arr[2]));
                skills.add(attackSkill);
            }

            card.setSkills(skills);
            card.setWeaknessType(EnergyType.valueOf(br.readLine().split("\\. ")[1]));

            String resist = br.readLine().split("\\. ")[1];
            if (!resist.equals("-")){
                card.setResistanceType(EnergyType.valueOf(br.readLine().split("\\. ")[1]));
            }

            String runCost = br.readLine().split("\\. ")[1];
            card.setRetreatCost(runCost);

            String cardsetName = br.readLine().split("\\. ")[1];
            card.setGameSet(cardsetName);

            card.setRegulationMark(br.readLine().split("\\. ")[1].charAt(0));
            String line12 = br.readLine().split("\\. ")[1];

            String[] nameStudent = line12.split(" / ");
            card.setPokemonOwner(new Student(nameStudent[0],
                    nameStudent[1], nameStudent[2], nameStudent[3]));

        }
        catch (IOException e) {
            System.err.println("Ошибка имортирования: " + e.getMessage());
        }

        return card;
    }

    public Card importCardByte(String fileName) {
        Card card = null;

        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            card = (Card) objectIn.readObject();
        }
        catch (IOException e) {
            System.err.println("Ошибка импортирования: " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.err.println("Класс карты не найден: " + e.getMessage());
        }

        return card;
    }
}
