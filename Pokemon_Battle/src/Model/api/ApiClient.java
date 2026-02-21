package Model.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import Model.Pokemon;
import Model.Trainer;
import Model.Movement;
import Model.Type;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8000/api";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private static final Gson gson = new Gson();
    private static String authToken;
    private int trainerId;

    // ======================== AUTENTICACIÓN ========================

    public String login(String username, String password) throws IOException, InterruptedException {
        String jsonPayload = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}",
            username, password
        );

        String response = post("/auth/login", jsonPayload);
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        authToken = jsonResponse.get("access_token").getAsString();
        return authToken;
    }

    public String register(String username, String password) throws IOException, InterruptedException {
        String jsonPayload = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}",
            username, password
        );

        String response = post("/auth/register", jsonPayload);
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        authToken = jsonResponse.get("access_token").getAsString();
        return authToken;
    }

    // ======================== ENTRENADOR ========================

    public Trainer getTrainerProfile(int suId) throws IOException, InterruptedException {
        String response = getWithAuth("/trainers/" + suId);
        JsonObject jsonTrainer = gson.fromJson(response, JsonObject.class);
        this.trainerId = jsonTrainer.get("suid").getAsInt();
        return parseTrainerFromJson(jsonTrainer);
    }

    public Trainer getMyProfile() throws IOException, InterruptedException {
        String response = getWithAuth("/trainers/me");
        JsonObject jsonTrainer = gson.fromJson(response, JsonObject.class);
        this.trainerId = jsonTrainer.get("suid").getAsInt();
        return parseTrainerFromJson(jsonTrainer);
    }

    public int createTrainer(String nickname) throws IOException, InterruptedException {
        String jsonPayload = String.format(
            "{\"name\":\"%s\"}",
            nickname
        );

        String response = postWithAuth("/trainers/", jsonPayload);
        JsonObject jsonTrainer = gson.fromJson(response, JsonObject.class);
        JsonObject trainer = jsonTrainer.getAsJsonObject("trainer");
        this.trainerId = trainer.get("suid").getAsInt();
        return this.trainerId;
    }

    // ======================== EQUIPO Y BATALLA ========================

    public Pokemon[] getTeam(int suId) throws IOException, InterruptedException {
        String response;
        if (authToken != null) {
            try {
                response = getWithAuth("/trainers/" + suId + "/team");
            } catch (IOException e) {
                response = get("/trainers/" + suId + "/team");
            }
        } else {
            response = get("/trainers/" + suId + "/team");
        }
        com.google.gson.JsonElement element = gson.fromJson(response, com.google.gson.JsonElement.class);
        if (element != null && element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("team") && obj.get("team").isJsonArray()) {
                return parseTeamFromJson(obj.getAsJsonArray("team"));
            }
        }
        if (element != null && element.isJsonArray()) {
            return parseTeamFromJson(element.getAsJsonArray());
        }
        return new Pokemon[0];
    }

    public Pokemon[] getInventory(int suId) throws IOException, InterruptedException {
        String response;
        if (authToken != null) {
            try {
                response = getWithAuth("/trainers/" + suId + "/inventory");
            } catch (IOException e) {
                response = get("/trainers/" + suId + "/inventory");
            }
        } else {
            response = get("/trainers/" + suId + "/inventory");
        }
        com.google.gson.JsonElement element = gson.fromJson(response, com.google.gson.JsonElement.class);
        if (element != null && element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("inventory") && obj.get("inventory").isJsonArray()) {
                return parseTeamFromJson(obj.getAsJsonArray("inventory"));
            }
        }
        if (element != null && element.isJsonArray()) {
            return parseTeamFromJson(element.getAsJsonArray());
        }
        return new Pokemon[0];
    }

    public void recordBattleWin(int suId) throws IOException, InterruptedException {
        if (authToken != null) {
            try {
                postWithAuth("/trainers/" + suId + "/stats/win", "{}");
                return;
            } catch (IOException e) {
                // Fallback to non-auth if auth token is invalid
            }
        }
        post("/trainers/" + suId + "/stats/win", "{}");
    }

    public void recordBattleLoss(int suId) throws IOException, InterruptedException {
        if (authToken != null) {
            try {
                postWithAuth("/trainers/" + suId + "/stats/loss", "{}");
                return;
            } catch (IOException e) {
                // Fallback to non-auth if auth token is invalid
            }
        }
        post("/trainers/" + suId + "/stats/loss", "{}");
    }

    public void updatePokemonExperience(int suId, int pokemonIndex, long experience) throws IOException, InterruptedException {
        String jsonPayload = String.format(
            "{\"experience\":%d}",
            experience
        );
        if (authToken != null) {
            try {
                putWithAuth("/trainers/" + suId + "/pokemons/" + pokemonIndex, jsonPayload);
                return;
            } catch (IOException e) {
                // Fallback to non-auth if auth token is invalid
            }
        }
        put("/trainers/" + suId + "/pokemons/" + pokemonIndex, jsonPayload);
    }

    public void addPokemonToTrainer(int suId, String pokedexId) throws IOException, InterruptedException {
        String jsonPayload = String.format(
            "{\"pokedex_id\":\"%s\"}",
            pokedexId
        );
        postWithAuth("/trainers/" + suId + "/pokemon", jsonPayload);
    }

    // ======================== BÚSQUEDA DE ENTRENADORES ========================

    public Trainer searchTrainerByName(String name) throws IOException, InterruptedException {
        // Obtener lista de todos los trainers (si existe el endpoint)
        // Por ahora, intentamos con el nombre como búsqueda
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String response;
            if (authToken != null) {
                try {
                    response = getWithAuth("/trainers/search?name=" + encodedName);
                } catch (IOException e) {
                    response = get("/trainers/search?name=" + encodedName);
                }
            } else {
                response = get("/trainers/search?name=" + encodedName);
            }
            System.out.println("[ApiClient] searchTrainerByName name=" + name + " encoded=" + encodedName);
            System.out.println("[ApiClient] searchTrainerByName response=" + response);
            Trainer trainer = parseTrainerSearchResponse(response, name);
            if (trainer != null) {
                this.trainerId = trainer.getSuid();
            }
            return trainer;
        } catch (IOException e) {
            // Si no existe endpoint de búsqueda, retornar null
            return null;
        }
    }

    private Trainer parseTrainerSearchResponse(String response, String name) {
        String target = name == null ? "" : name.trim().toLowerCase();
        if (target.isEmpty()) {
            return null;
        }

        com.google.gson.JsonElement element = gson.fromJson(response, com.google.gson.JsonElement.class);
        if (element == null || element.isJsonNull()) {
            return null;
        }

        System.out.println("[ApiClient] parseTrainerSearchResponse target=" + target + " elementType=" + (element.isJsonArray() ? "array" : element.isJsonObject() ? "object" : "other"));
        if (element.isJsonArray()) {
            return findExactTrainerInArray(element.getAsJsonArray(), target);
        }

        if (!element.isJsonObject()) {
            return null;
        }

        JsonObject obj = element.getAsJsonObject();
        if (obj.has("trainers") && obj.get("trainers").isJsonArray()) {
            return findExactTrainerInArray(obj.getAsJsonArray("trainers"), target);
        }

        if (obj.has("trainer") && obj.get("trainer").isJsonObject()) {
            JsonObject trainerObj = obj.getAsJsonObject("trainer");
            if (isExactTrainerMatch(trainerObj, target)) {
                return parseTrainerFromJson(trainerObj);
            }
            return null;
        }

        if (obj.has("suid")) {
            if (isExactTrainerMatch(obj, target)) {
                return parseTrainerFromJson(obj);
            }
            return null;
        }

        return null;
    }

    private Trainer findExactTrainerInArray(JsonArray array, String target) {
        for (int i = 0; i < array.size(); i++) {
            JsonObject candidate = array.get(i).getAsJsonObject();
            if (isExactTrainerMatch(candidate, target)) {
                return parseTrainerFromJson(candidate);
            }
        }
        return null;
    }

    private boolean isExactTrainerMatch(JsonObject candidate, String target) {
        String candidateName = candidate.has("name") ? candidate.get("name").getAsString() : "";
        String candidateUsername = candidate.has("username") ? candidate.get("username").getAsString() : "";
        return candidateName.trim().toLowerCase().equals(target)
                || candidateUsername.trim().toLowerCase().equals(target);
    }

    public Pokemon[] loadTeamFromAPI(int suId) throws IOException, InterruptedException {
        return getTeam(suId);
    }

    public Pokemon[] loadInventoryFromAPI(int suId) throws IOException, InterruptedException {
        return getInventory(suId);
    }

    // ======================== MÉTODOS HTTP GENÉRICOS ========================

    private String getWithAuth(String endpoint) throws IOException, InterruptedException {
        if (authToken == null) {
            throw new IOException("No authentication token. Please login first.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            throw new IOException("Error en solicitud GET a " + endpoint + ": " + httpResponse.statusCode() + " - " + httpResponse.body());
        }
        
        return httpResponse.body();
    }

    private String postWithAuth(String endpoint, String json) throws IOException, InterruptedException {
        if (authToken == null) {
            throw new IOException("No authentication token. Please login first.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            throw new IOException("Error en solicitud POST a " + endpoint + ": " + httpResponse.statusCode() + " - " + httpResponse.body());
        }
        
        return httpResponse.body();
    }

    private String putWithAuth(String endpoint, String json) throws IOException, InterruptedException {
        if (authToken == null) {
            throw new IOException("No authentication token. Please login first.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            throw new IOException("Error en solicitud PUT a " + endpoint + ": " + httpResponse.statusCode() + " - " + httpResponse.body());
        }
        
        return httpResponse.body();
    }

    // GET genérico (sin autenticación)
    public static String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Error en solicitud GET a " + endpoint + ": " + response.statusCode());
        }
        
        return response.body();
    }

    // POST genérico (sin autenticación)
    public static String post(String endpoint, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Error en solicitud POST a " + endpoint + ": " + response.statusCode());
        }
        
        return response.body();
    }

    // PUT genérico
    public static String put(String endpoint, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Error en solicitud PUT a " + endpoint + ": " + response.statusCode());
        }
        
        return response.body();
    }

    // Obtener lista de Pokémon
    public static String getPokemonList() throws IOException, InterruptedException {
        return get("/pokemons/");
    }

    // Obtener Pokémon específico
    public static String getPokemon(String pokedexId) throws IOException, InterruptedException {
        return get("/pokemons/" + pokedexId);
    }

    // ======================== PARSEO JSON ========================

    private Trainer parseTrainerFromJson(JsonObject json) {
        Trainer trainer = new Trainer();
        
        trainer.setId(String.valueOf(json.get("suid").getAsInt()));
        trainer.setName(json.get("name").getAsString());
        trainer.setWins(json.get("wins").getAsInt());
        trainer.setLosses(json.get("losses").getAsInt());
        
        // Parsear equipo si existe
        if (json.has("team_detail") && !json.get("team_detail").isJsonNull()) {
            JsonArray teamDetail = json.getAsJsonArray("team_detail");
            ArrayList<Pokemon> teamList = new ArrayList<>();
            for (int i = 0; i < teamDetail.size(); i++) {
                Pokemon poke = parsePokemonFromJson(teamDetail.get(i).getAsJsonObject());
                teamList.add(poke);
            }
            trainer.setPokemonTeam(teamList);
        }
        
        return trainer;
    }

    private Pokemon[] parseTeamFromJson(JsonArray jsonArray) {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonPokemon = jsonArray.get(i).getAsJsonObject();
            Pokemon pokemon = parsePokemonFromJson(jsonPokemon);
            pokemonList.add(pokemon);
        }
        
        return pokemonList.toArray(new Pokemon[0]);
    }

    private Pokemon parsePokemonFromJson(JsonObject json) {
        String pokedexId = json.get("pokedex_id").getAsString();
        String name = json.get("name").getAsString();
        
        // Obtener tipo
        Type type = Type.NORMAL;
        if (json.has("type")) {
            String typeStr = json.get("type").getAsString();
            try {
                type = Type.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                type = Type.NORMAL;
            }
        }
        
        // Stats base
        short baseHp = json.has("base_hp") ? json.get("base_hp").getAsShort() : 45;
        short baseAttack = json.has("base_attack") ? json.get("base_attack").getAsShort() : 49;
        short baseSpAtk = json.has("base_special_attack") ? json.get("base_special_attack").getAsShort() : 65;
        short baseDef = json.has("base_defense") ? json.get("base_defense").getAsShort() : 49;
        short baseSpDef = json.has("base_special_defense") ? json.get("base_special_defense").getAsShort() : 65;
        short baseSpeed = json.has("base_speed") ? json.get("base_speed").getAsShort() : 45;
        
        // Movimientos
        ArrayList<Movement> moves = new ArrayList<>();
        if (json.has("moves")) {
            JsonArray movesArray = json.getAsJsonArray("moves");
            for (int i = 0; i < movesArray.size(); i++) {
                JsonObject moveJson = movesArray.get(i).getAsJsonObject();
                Movement move = new Movement(
                    moveJson.get("name").getAsString(),
                    moveJson.get("type").getAsString(),
                    moveJson.get("power").getAsInt(),
                    moveJson.get("accuracy").getAsInt()
                );
                moves.add(move);
            }
        }
        
        Pokemon pokemon = new Pokemon(pokedexId, name, type, baseHp, baseAttack, baseSpAtk, baseDef, baseSpDef, baseSpeed, moves);
        
        // Establecer experiencia si existe
        if (json.has("experience")) {
            pokemon.setExperience(json.get("experience").getAsLong());
        }
        if (json.has("next_level_exp")) {
            pokemon.setNextLevelExp(json.get("next_level_exp").getAsLong());
        }
        
        // Establecer sprites
        if (json.has("front_sprite")) {
            pokemon.setFrontSprite(json.get("front_sprite").getAsString());
        }
        if (json.has("back_sprite")) {
            pokemon.setBackSprite(json.get("back_sprite").getAsString());
        }
        
        return pokemon;
    }

    // ======================== GETTERS Y SETTERS ========================

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int id) {
        this.trainerId = id;
    }
}