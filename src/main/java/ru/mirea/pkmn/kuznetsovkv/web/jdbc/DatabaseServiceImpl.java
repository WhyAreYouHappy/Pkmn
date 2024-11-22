package ru.mirea.pkmn.kuznetsovkv.web.jdbc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.mirea.pkmn.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseServiceImpl implements DatabaseService {

    private final Connection connection;
    private final Properties databaseProperties;
    private static final Gson GSON = new Gson();
    private static final String DATABASE_PROPERTIES_PATH = "src/main/resources/database.properties";

    public DatabaseServiceImpl() throws SQLException, IOException {

        databaseProperties = new Properties();
        try (FileInputStream propertiesFile = new FileInputStream(DATABASE_PROPERTIES_PATH)) {
            databaseProperties.load(propertiesFile);
        }

        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );

        System.out.println("Connection is " + (connection.isValid(0) ? "up" : "down"));
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setName(rs.getString("name"));
        card.setNumber(rs.getString("card_number"));
        card.setPokemonStage(PokemonStage.valueOf(rs.getString("stage")));
        card.setHp(rs.getInt("hp"));
        card.setPokemonType(EnergyType.valueOf(rs.getString("pokemon_type")));

        String evolvesFrom = rs.getString("evolves_from");
        card.setEvolvesFrom(evolvesFrom != null ? getCardFromDatabaseByUUID(UUID.fromString(evolvesFrom)) : null);

        Type type = new TypeToken<List<AttackSkill>>() {}.getType();
        card.setSkills(GSON.fromJson(rs.getString("attack_skills"), type));

        card.setWeaknessType(rs.getString("weakness_type") != null
                ? EnergyType.valueOf(rs.getString("weakness_type")) : null);
        card.setResistanceType(rs.getString("resistance_type") != null
                ? EnergyType.valueOf(rs.getString("resistance_type")) : null);

        card.setRetreatCost(rs.getString("retreat_cost"));
        card.setGameSet(rs.getString("game_set"));
        card.setRegulationMark(rs.getString("regulation_mark").charAt(0));

        String pokemonOwner = rs.getString("pokemon_owner");
        card.setPokemonOwner(pokemonOwner != null ? getStudentFromDatabaseById(UUID.fromString(pokemonOwner)) : null);

        return card;
    }

    @Override
    public Card getCardFromDatabase(String cardName) throws SQLException {
        String query = "SELECT * FROM card WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cardName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToCard(rs) : null;
            }
        }
    }

    private Card getCardFromDatabaseByUUID(UUID cardId) throws SQLException {
        String query = "SELECT * FROM card WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToCard(rs) : null;
            }
        }
    }

    @Override
    public Student getStudentFromDatabaseById(UUID uuid) throws SQLException {
        String query = "SELECT * FROM student WHERE \"id\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToStudent(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public Student getStudentFromDatabase(String studentName) throws SQLException {
        if (studentName == null) return null;

        String[] split = studentName.split(" ");
        String query = "SELECT * FROM student WHERE \"familyName\" = ? AND \"firstName\" = ? AND \"patronicName\" = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, split[0]);
            ps.setString(2, split[1]);
            ps.setString(3, split[2]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToStudent(rs) : null;
            }
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student(
                rs.getString("firstName"),
                rs.getString("familyName"),
                rs.getString("patronicName"),
                rs.getString("group")
        );
        return student;
    }


    @Override
    public void saveCardToDatabase(Card card) throws SQLException {
        UUID evolvesFromId = null;
        if (card.getEvolvesFrom() != null) {
            evolvesFromId = getCardIdFromDatabase(card.getEvolvesFrom().getName());
            if (evolvesFromId == null) {
                saveCardToDatabase(card.getEvolvesFrom());
                evolvesFromId = getCardIdFromDatabase(card.getEvolvesFrom().getName());
            }
        }

        UUID ownerId = null;
        if (card.getPokemonOwner() != null) {
            ownerId = getStudentIdFromDatabase(card.getPokemonOwner());
            if (ownerId == null) {
                createPokemonOwner(card.getPokemonOwner());
                ownerId = getStudentIdFromDatabase(card.getPokemonOwner());
            }
        }

        String query = "INSERT INTO card(id, name, hp, evolves_from, game_set, pokemon_owner, stage, retreat_cost, " +
                "weakness_type, resistance_type, attack_skills, pokemon_type, regulation_mark, card_number) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::json, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1, UUID.randomUUID());
            ps.setString(2, card.getName());
            ps.setInt(3, card.getHp());
            ps.setObject(4, evolvesFromId);
            ps.setObject(5, card.getGameSet());
            ps.setObject(6, ownerId);
            ps.setString(7, card.getPokemonStage().name());
            ps.setString(8, card.getRetreatCost());
            ps.setString(9, card.getWeaknessType() != null ? card.getWeaknessType().name() : null);
            ps.setString(10, card.getResistanceType() != null ? card.getResistanceType().name() : null);
            ps.setString(11, GSON.toJson(card.getSkills()));
            ps.setString(12, card.getPokemonType().name());
            ps.setString(13, String.valueOf(card.getRegulationMark()));
            ps.setString(14, card.getNumber());
            ps.executeUpdate();
        }
    }

    private UUID getCardIdFromDatabase(String cardName) throws SQLException {
        System.out.println("FUCKING FUCK");
        String query = "SELECT * FROM card WHERE \"name\" = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cardName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString("id"));
                }
            }
        }
        return null;
    }

    private UUID getStudentIdFromDatabase(Student student) throws SQLException {
        String query = "SELECT * FROM student WHERE \"familyName\" = ? AND \"firstName\" = ? AND \"patronicName\" = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, student.getSurName());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getFamilyName());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString("id"));
                }
            }
        }
        return null;
    }
    @Override
    public void createPokemonOwner(Student student) throws SQLException {
        String query = "INSERT INTO student(id, \"familyName\", \"firstName\", \"patronicName\", \"group\") VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1, UUID.randomUUID());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getSurName());
            ps.setString(4, student.getFamilyName());
            ps.setString(5, student.getGroup());
            ps.executeUpdate();
        }
    }
}