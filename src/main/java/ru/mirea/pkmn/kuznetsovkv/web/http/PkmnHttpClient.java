package ru.mirea.pkmn.kuznetsovkv.web.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PkmnHttpClient {
    private static final String BASE_URL = "https://api.pokemontcg.io";
    private final PokemonTcgAPI tcgAPI;

    public PkmnHttpClient() {
        this.tcgAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(new JsonMapper()))
                .build()
                .create(PokemonTcgAPI.class);
    }

    public JsonNode getPokemonCard(String name, String number) throws IOException {
        String requestQuery = String.format("name:\"%s\" number:%s", name, number);

        Response<JsonNode> response = tcgAPI.getPokemon(requestQuery).execute();

        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Ошибка при получении данных карты. Код ответа: " + response.code());
        }

        return response.body();
    }

    public Set<String> loadAttackDescriptions(String cardName, String cardNumber) {
        Set<String> descriptions = new HashSet<>();

        try {
            JsonNode cardNode = getPokemonCard(cardName, cardNumber);

            if (cardNode.has("attacks")) {
                cardNode.get("attacks").forEach(attack -> {
                    if (attack.has("text")) {
                        descriptions.add(attack.get("text").asText());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке описаний атак: " + e.getMessage());
            e.printStackTrace();
        }

        return descriptions;
    }
}