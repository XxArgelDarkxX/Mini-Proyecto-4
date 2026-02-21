package Controller;

import Model.Trainer;
import Model.Pokemon;
import Model.Movement;
import Model.Type;
import Model.api.ApiClient;
import View.Interfaces.TrainerView;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.ArrayList;

public class TrainerController {
    private TrainerView view;
    private final Trainer[] trainers;
    private boolean isGui;

    public TrainerController(TrainerView view, boolean isGui) {
        this.view = view;
        this.isGui = isGui;
        this.trainers = new Trainer[2];
        view.setController(this);
    }

    public void initialize() {
        view.showMenu();
    }

    public void proccessTrainers(String trainerName1, String trainerName2) {
        if(!view.validateNames(trainerName1, trainerName2)){
            view.validateNames(trainerName1, trainerName2);
        }else{
            try {
                ApiClient apiClient = new ApiClient();
                
                // Buscar entrenadores por nombre en la API
                Trainer trainer1 = apiClient.searchTrainerByName(trainerName1);
                Trainer trainer2 = apiClient.searchTrainerByName(trainerName2);
                
                // Si no existen en la API, crearlos
                if (trainer1 == null) {
                    trainer1 = new Trainer();
                    trainer1.setName(trainerName1);
                }
                
                if (trainer2 == null) {
                    trainer2 = new Trainer();
                    trainer2.setName(trainerName2);
                }
                
                // Cargar equipo de Pokémon de la API (si tienen SUID)
                if (trainer1.getSuid() > 0) {
                    Pokemon[] team1 = apiClient.loadTeamFromAPI(trainer1.getSuid());
                    trainer1.setPokemonTeam(buildTeamList(team1));
                } else {
                    // Si no existe en API, asignar Pokémon por defecto
                    assignDefaultPokemons(trainer1, apiClient);
                }
                
                if (trainer2.getSuid() > 0) {
                    Pokemon[] team2 = apiClient.loadTeamFromAPI(trainer2.getSuid());
                    trainer2.setPokemonTeam(buildTeamList(team2));
                } else {
                    // Si no existe en API, asignar Pokémon por defecto
                    assignDefaultPokemons(trainer2, apiClient);
                }
                
                trainers[0] = trainer1;
                trainers[1] = trainer2;
                
                System.out.println("Trainer 1: " + trainer1.getName() + " con " + trainer1.getPokemonTeam().size() + " Pokémon");
                System.out.println("Trainer 2: " + trainer2.getName() + " con " + trainer2.getPokemonTeam().size() + " Pokémon");
                
                view.nextView();
            } catch (Exception e) {
                System.err.println("Error al procesar entrenadores: " + e.getMessage());
                e.printStackTrace();
                view.showError("Error al conectar con la API. Verifica que FastAPI esté ejecutándose.");
            }
        }
    }
    
    private void assignDefaultPokemons(Trainer trainer, ApiClient apiClient) throws Exception {
        // Obtener lista de Pokémon disponibles
        String pokemonJsonList = apiClient.getPokemonList();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        com.google.gson.JsonObject response = gson.fromJson(pokemonJsonList, com.google.gson.JsonObject.class);
        com.google.gson.JsonArray pokemonArray = response.getAsJsonArray("pokemons");
        
        // Asignar los primeros 3 Pokémon
        for (int i = 0; i < 3 && i < pokemonArray.size(); i++) {
            com.google.gson.JsonObject pokemonJson = pokemonArray.get(i).getAsJsonObject();
            Pokemon pokemon = parsePokemonFromJson(pokemonJson);
            trainer.addPokemonToTeam(pokemon);
        }
    }

    private ArrayList<Pokemon> buildTeamList(Pokemon[] team) {
        ArrayList<Pokemon> list = new ArrayList<>();
        if (team == null) {
            return list;
        }
        for (int i = 0; i < team.length && i < 3; i++) {
            list.add(team[i]);
        }
        return list;
    }
    
    private Pokemon parsePokemonFromJson(com.google.gson.JsonObject json) {
        String pokedexId = json.get("pokedex_id").getAsString();
        String name = json.get("name").getAsString();
        String typeStr = json.get("type").getAsString();
        
        Model.Type type = Model.Type.NORMAL;
        try {
            type = Model.Type.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = Model.Type.NORMAL;
        }
        
        int baseHp = json.get("base_hp").getAsInt();
        int baseAttack = json.get("base_attack").getAsInt();
        int baseSpAtk = json.get("base_special_attack").getAsInt();
        int baseDef = json.get("base_defense").getAsInt();
        int baseSpDef = json.get("base_special_defense").getAsInt();
        int baseSpeed = json.get("base_speed").getAsInt();
        
        ArrayList<Model.Movement> moves = new ArrayList<>();
        com.google.gson.JsonArray movesArray = json.getAsJsonArray("moves");
        for (int i = 0; i < movesArray.size() && i < 4; i++) {
            com.google.gson.JsonObject moveJson = movesArray.get(i).getAsJsonObject();
            String moveName = moveJson.get("name").getAsString();
            String moveType = moveJson.get("type").getAsString();
            int power = moveJson.get("power").getAsInt();
            int accuracy = moveJson.get("accuracy").getAsInt();
            
            Model.Movement move = new Model.Movement(moveName, moveType, power, accuracy);
            moves.add(move);
        }
        
        Pokemon pokemon = new Pokemon(pokedexId, name, type, (short)baseHp, (short)baseAttack, (short)baseSpAtk, (short)baseDef, (short)baseSpDef, (short)baseSpeed, moves);
        
        // Cargar sprites si existen
        if (json.has("front_sprite")) {
            pokemon.setFrontSprite(json.get("front_sprite").getAsString());
        }
        if (json.has("back_sprite")) {
            pokemon.setBackSprite(json.get("back_sprite").getAsString());
        }
        
        return pokemon;
    }

    public void changeView() {
        if (isGui) {
            view = new View.Console.TrainerConsoleView();
        } else {
            view = new View.GUI.TrainerGUIView();
        }
        isGui = !isGui;
        view.setController(this);
        view.showMenu();
    }

    public void clearFields() {
        view.clearFields();
    }

    public Trainer[] getTrainers() {
        return trainers;
    }
}