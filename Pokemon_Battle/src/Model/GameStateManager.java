package Model;

import Model.Data.Data;
import Model.Exceptions.pokemonBattle.indexErrorException;
import Model.Exceptions.pokemonBattle.pokemonNotFoundException;
import Model.Exceptions.pokemonBattle.quantityCheckException;
import java.io.*;
import java.util.ArrayList;

public class GameStateManager {
    private static final String SAVE_DIRECTORY = "saves/";

    public GameStateManager() {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Data.initializeData();
    }

    public void saveGame(String filename, PokemonBattle battle) throws IOException {
        String filepath = SAVE_DIRECTORY + filename + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            // Guardar entrenador 1
            writer.println("### TRAINER 1 ###");
            saveTrainer(writer, battle.getTrainers()[0], battle.getCurrentPokemonIndex(0));
            
            // Guardar entrenador 2
            writer.println("### TRAINER 2 ###");
            saveTrainer(writer, battle.getTrainers()[1], battle.getCurrentPokemonIndex(1));
        }
    }

    private void saveTrainer(PrintWriter writer, Trainer trainer, byte currentPokemonIndex) {
        writer.println(trainer.getName());
        writer.println(currentPokemonIndex);
        // Guardar cada Pokemon del equipo
        for (Pokemon pokemon : trainer.getPokemonTeam()) {
            writer.println(pokemon.getPokedexId() + "," + pokemon.getHp() + "," + pokemon.getMaxHp());
        }
    }

    public PokemonBattle loadGame(String filename) throws IOException, quantityCheckException, pokemonNotFoundException, indexErrorException {
        String filepath = SAVE_DIRECTORY + filename + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            Trainer[] trainers = new Trainer[2];
            Pokemon[] initialPokemons = new Pokemon[2];
            byte[] currentIndexes = new byte[2];

            // Leer entrenador 1
            reader.readLine(); // Leer "### TRAINER 1 ###"
            String trainerName = reader.readLine();
            byte currentIndex = Byte.parseByte(reader.readLine());
            ArrayList<Pokemon> team1 = new ArrayList<>();
            
            // Leer equipo del entrenador 1
            for (int i = 0; i < 3; i++) {
                String[] pokemonData = reader.readLine().split(",");
                Pokemon pokemon = createPokemonFromId(pokemonData[0]);
                pokemon.setHp(Short.parseShort(pokemonData[1]));
                pokemon.setMaxHp(Short.parseShort(pokemonData[2]));
                System.out.println(pokemon.getHp() + "/" + pokemon.getMaxHp());
                team1.add(pokemon);
            }
            trainers[0] = new Trainer(trainerName, team1);

            // Si el Pokemon actual está debilitado, buscar el siguiente disponible
            while (currentIndex < team1.size() && team1.get(currentIndex).getHp() <= 0) {
                currentIndex++;
            }
            currentIndexes[0] = currentIndex < team1.size() ? currentIndex : 0;

            // Leer entrenador 2 (similar al entrenador 1)
            reader.readLine();
            trainerName = reader.readLine();
            currentIndex = Byte.parseByte(reader.readLine());
            ArrayList<Pokemon> team2 = new ArrayList<>();
            
            for (int i = 0; i < 3; i++) {
                String[] pokemonData = reader.readLine().split(",");
                Pokemon pokemon = createPokemonFromId(pokemonData[0]);
                pokemon.setHp(Short.parseShort(pokemonData[1]));
                pokemon.setMaxHp(Short.parseShort(pokemonData[2]));
                team2.add(pokemon);
            }
            trainers[1] = new Trainer(trainerName, team2);

            while (currentIndex < team2.size() && team2.get(currentIndex).getHp() <= 0) {
                currentIndex++;
            }
            currentIndexes[1] = currentIndex < team2.size() ? currentIndex : 0;

            // Inicializar Pokemon iniciales con los que tienen vida
            initialPokemons[0] = team1.get(currentIndexes[0]);
            initialPokemons[1] = team2.get(currentIndexes[1]);

            PokemonBattle battle = new PokemonBattle(trainers, initialPokemons);
            battle.selectPokemon((byte)0, currentIndexes[0]);
            battle.selectPokemon((byte)1, currentIndexes[1]);
            
            return battle;
        }
    }

    private Pokemon createPokemonFromId(String pokedexId) {
        ArrayList<Pokemon> pokemons = Data.getPokemons();
        
        // Buscar el Pokémon con el ID correspondiente
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getPokedexId().equals(pokedexId)) {
                // Crear una nueva instancia del Pokémon para evitar referencias
                Pokemon newPokemon = new Pokemon(
                    pokemon.getPokedexId(),
                    pokemon.getName(),
                    pokemon.getType(),
                    pokemon.getHp(),
                    pokemon.getAttack(),
                    pokemon.getSpecialAttack(),
                    pokemon.getDefense(),
                    pokemon.getSpecialDefense(),
                    pokemon.getSpeed(),
                    new ArrayList<>(pokemon.getMoves())
                );
                
                return newPokemon;
            }
        }
        
        throw new IllegalArgumentException("Pokemon no encontrado con ID: " + pokedexId);
    }

    public String[] listSavedGames() {
        File directory = new File(SAVE_DIRECTORY);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return new String[0];
        
        String[] saveFiles = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            saveFiles[i] = files[i].getName().replace(".txt", "");
        }
        return saveFiles;
    }
}