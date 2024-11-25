package ru.mirea.pkmn.kuznetsovkv.web.jdbc;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.postgresql.util.PGobject;
import ru.mirea.pkmn.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseServiceImpl implements DatabaseService {

    private final Connection connection;
    private final Properties databaseProperties;

    public DatabaseServiceImpl() throws SQLException, IOException {
        databaseProperties = loadProperties("src/main/resources/database.properties");
        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );
    }

    @Override
    public Card getCardFromDatabase(String cardName) {
        String query = "SELECT * FROM card WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cardName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? mapCardFromResultSet(resultSet) : null;
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Ошибка при получении карты", e);
        }
    }

    @Override
    public Student getStudentFromDatabase(String studentFullName) {
        String query = "SELECT \"familyName\", \"firstName\", \"patronicName\", \"group\" " +
                "FROM student WHERE \"familyName\" = ? AND \"firstName\" = ? AND \"patronicName\" = ?";


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String[] nameParts = studentFullName.split(" ");
            for (int i = 0; i < 3; i++) preparedStatement.setString(i + 1, nameParts[i]);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? mapStudentFromResultSet(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении имени владельца", e);
        }
    }

    private Student getPokemonOwner(UUID ownerUUID){
        String query = "SELECT * FROM student WHERE id = ?";
        Properties databaseProperties = new Properties();

        try{
            databaseProperties.load(new FileInputStream("src/main/resources/database.properties"));
            try (Connection connection = DriverManager.getConnection(
                    databaseProperties.getProperty("database.url"),
                    databaseProperties.getProperty("database.user"),
                    databaseProperties.getProperty("database.password"));
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, ownerUUID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Student student = new Student();

                        student.setFamilyName(resultSet.getString("familyName"));
                        student.setFirstName(resultSet.getString("firstName"));
                        student.setSurName(resultSet.getString("patronicName"));
                        student.setGroup(resultSet.getString("group"));

                        return student;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Ошибка получения карты из базы данных: " + e.getMessage());
                return null;
            }
            return null;
        } catch (IOException e) {
            System.err.println("Ошибка загрузки файла: " + e.getMessage());
        }
        return null;
    }
    @Override
    public void saveCardToDatabase(Card card) {
        Student owner = card.getPokemonOwner();
        UUID ownerUUID = fetchOwnerUUID(owner.getFamilyName());

        if (ownerUUID != null) {
            String query = "INSERT INTO card (id, name, hp, evolves_from, game_set, pokemon_owner, " +
                    "stage, retreat_cost, weakness_type, resistance_type, attack_skills, pokemon_type, " +
                    "regulation_mark, card_number) VALUES (gen_random_uuid(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                populateCardInsertStatement(statement, card, ownerUUID);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) System.out.println("Данные успешно добавлены");
            } catch (SQLException | IOException e) {
                throw new RuntimeException("Ошибка добавления инфы о карте", e);
            }
        } else {
            System.out.println("Студент не найден");
        }
    }

    @Override
    public void createPokemonOwner(Student owner) {
        String query = "INSERT INTO student (id, \"familyName\", \"firstName\", \"patronicName\", \"group\") " +
                "VALUES (gen_random_uuid(), ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            populateStudentInsertStatement(preparedStatement, owner);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления студента", e);
        }
    }

    private Properties loadProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        }
        return properties;
    }

    private UUID fetchOwnerUUID(String familyName) {
        String query = "SELECT id FROM student WHERE \"familyName\" = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, familyName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? UUID.fromString(resultSet.getString("id")) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска по UUID", e);
        }
    }

    private Card mapCardFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        Card card = new Card();
        card.setName(resultSet.getString("name"));
        card.setHp(resultSet.getInt("hp"));
        card.setEvolvesFrom(getCardByUUID(resultSet.getString("evolves_from")));
        card.setGameSet(resultSet.getString("game_set"));
        card.setPokemonStage(PokemonStage.valueOf(resultSet.getString("stage")));
        card.setRetreatCost(resultSet.getString("retreat_cost"));
        card.setWeaknessType(EnergyType.valueOf(resultSet.getString("weakness_type")));
        card.setResistanceType(getEnergyType(resultSet.getString("resistance_type")));
        card.setSkills(new Gson().fromJson(resultSet.getString("attack_skills"), new TypeToken<List<AttackSkill>>() {}.getType()));
        card.setPokemonType(EnergyType.valueOf(resultSet.getString("pokemon_type")));
        card.setRegulationMark(resultSet.getString("regulation_mark").charAt(0));
        card.setNumber(resultSet.getString("card_number"));
        card.setPokemonOwner(getStudentByUUID(resultSet.getString("pokemon_owner")));
        return card;
    }

    private Student mapStudentFromResultSet(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getString("familyName"),
                resultSet.getString("firstName"),
                resultSet.getString("patronicName"),
                resultSet.getString("group")
        );
    }

    private void populateCardInsertStatement(PreparedStatement statement, Card card, UUID ownerUUID) throws SQLException, IOException {
        statement.setString(1, card.getName());
        statement.setInt(2, card.getHp());
        statement.setObject(3, card.getEvolvesFrom() != null ? UUID.fromString(card.getEvolvesFrom().getName()) : null);
        statement.setString(4, card.getGameSet());
        statement.setObject(5, ownerUUID);
        statement.setString(6, card.getPokemonStage().name());
        statement.setString(7, card.getRetreatCost());
        statement.setString(8, card.getWeaknessType().name());
        statement.setString(9, card.getResistanceType() != null ? card.getResistanceType().name() : null);
        statement.setObject(10, toPGJson(card.getSkills()));
        statement.setString(11, card.getPokemonType().name());
        statement.setString(12, String.valueOf(card.getRegulationMark()));
        statement.setString(13, card.getNumber());
    }

    private void populateStudentInsertStatement(PreparedStatement statement, Student owner) throws SQLException {
        statement.setString(1, owner.getFamilyName());
        statement.setString(2, owner.getFirstName());
        statement.setString(3, owner.getSurName());
        statement.setString(4, owner.getGroup());
    }

    private PGobject toPGJson(List<AttackSkill> skills) throws IOException, SQLException {
        PGobject pgObject = new PGobject();
        pgObject.setType("json");
        pgObject.setValue(new Gson().toJson(skills));
        return pgObject;
    }

    private Card getCardByUUID(String uuid) throws IOException {
        return uuid != null ? getCardFromDatabase(uuid) : null;
    }

    private EnergyType getEnergyType(String value) {
        return value != null && !value.isEmpty() ? EnergyType.valueOf(value) : null;
    }

    private Student getStudentByUUID(String uuid) {
        return uuid != null ? getPokemonOwner(UUID.fromString(uuid)) : null;
    }
}