package ru.mirea.pkmn.kuznetsovkv.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseService {
    Card getCardFromDatabase(String cardName) throws IOException;

    Student getStudentFromDatabase(String studentFullName);

    void saveCardToDatabase(Card card) throws IOException;

    void createPokemonOwner(Student owner);
}
