package ru.mirea.pkmn.kuznetsovkv;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.AttackSkill;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.kuznetsovkv.web.http.PkmnHttpClient;
import ru.mirea.pkmn.kuznetsovkv.web.jdbc.DatabaseServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PkmnApplication {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            executeCardOperations();
        } catch (IOException | SQLException e) {
            System.err.println("Ошибка при выполнении операции: " + e.getMessage());
        }
    }

    private static void executeCardOperations() throws IOException, SQLException {
        Card loadedCard = loadCardFromFile("src\\main\\resources\\my_card_no_numbers.txt");

        if (loadedCard != null) {
            displayLoadedCard(loadedCard);
            displayEvolutionDetails(loadedCard);

            PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
            JsonNode cardJson = fetchCardJson(pkmnHttpClient, loadedCard);

            updateCardSkillsFromJson(loadedCard, cardJson);
            exportCard(loadedCard);
            deserializeAndDisplayCard("src\\main\\resources\\Morpeko.crd");



            DatabaseServiceImpl dbService = new DatabaseServiceImpl();

            dbService.createPokemonOwner(loadedCard.getPokemonOwner());
            dbService.saveCardToDatabase(loadedCard);


            searchCardInDatabase(dbService, "Morpeko");
            searchStudentInDatabase(dbService, "Кузнецов Кирилл Владимирович");
        } else {
            System.out.println("Данные не найдены.");
        }
    }

    private static Card loadCardFromFile(String filePath) {
        CardImport cardImport = new CardImport(filePath);
        return cardImport.loadCard();
    }

    private static void displayLoadedCard(Card card) {
        //System.out.println("from my_card.txt");
        //System.out.println(card);
    }

    private static void displayEvolutionDetails(Card card) {
        if (card.getEvolvesFrom() != null) {
            //System.out.println("Эволюционирует из:");
            //System.out.println(card.getEvolvesFrom());
        }
    }

    private static JsonNode fetchCardJson(PkmnHttpClient httpClient, Card card) throws IOException {
        JsonNode cardJson = httpClient.getPokemonCard(card.getName(), card.getNumber());
        System.out.println("JSON-инфо покемона");
        System.out.println(cardJson.toPrettyString());
        return cardJson;
    }

    private static void updateCardSkillsFromJson(Card card, JsonNode cardJson) {
        Set<String> attackDescriptions = extractAttackDescriptionsFromJson(cardJson);
        List<AttackSkill> skills = card.getSkills();

        int skillsToUpdate = Math.min(skills.size(), attackDescriptions.size());
        List<String> attackDescriptionsList = new ArrayList<>(attackDescriptions);

        for (int i = 0; i < skillsToUpdate; i++) {
            skills.get(i).setDescription(attackDescriptionsList.get(i));
        }

        System.out.println("Карточка из JSON-версии покемончика");
        System.out.println(card);
    }

    private static Set<String> extractAttackDescriptionsFromJson(JsonNode cardJson) {
        return cardJson.findValues("attacks")
                .stream()
                .flatMap(attack -> attack.findValues("text").stream())
                .map(JsonNode::toPrettyString)
                .collect(Collectors.toSet());
    }

    private static void exportCard(Card card) {
        CardExport cardExport = new CardExport();
        cardExport.saveCard(card);

        if (card.getEvolvesFrom() != null) {
            CardExport evolvesExport = new CardExport();
            evolvesExport.saveCard(card.getEvolvesFrom());
        }
    }

    private static void deserializeAndDisplayCard(String filePath) { // десериализация
        CardImport cardImport = new CardImport(filePath);
        Card deserializedCard = cardImport.deserializeCard(filePath);

        if (deserializedCard != null) {
            //System.out.println("Десериализация: ");
            //System.out.println(deserializedCard);
        }
    }

    private static void searchCardInDatabase(DatabaseServiceImpl dbService, String cardName) throws SQLException, IOException {
        System.out.println("Покемон из БД: "); // достаём из БД по имени карты
        System.out.println(dbService.getCardFromDatabase(cardName));
    }

    private static void searchStudentInDatabase(DatabaseServiceImpl dbService, String studentName) throws SQLException {
        System.out.println("Студент из БД"); // вынимаем из БД по имени студента
        System.out.println(dbService.getStudentFromDatabase(studentName));
    }
}