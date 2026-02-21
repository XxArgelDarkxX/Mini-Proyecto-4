package Model;

import java.util.ArrayList;

public class Trainer {

    private String id;           // SUID del API
    private int suid;           // Alias para el SUID
    private String username;    // Usuario del API
    private String name;        // Nombre del entrenador
    private int sprite;         // Número de sprite (1-8)
    private int wins;
    private int losses;
    private ArrayList<Pokemon> pokemonTeam = new ArrayList<>();
    private ArrayList<Pokemon> inventory = new ArrayList<>();

    public Trainer() {}

    public Trainer(String id, String name, int wins, int losses, ArrayList<Pokemon> team) {
        this.id = id;
        this.suid = Integer.parseInt(id);
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.pokemonTeam = team;
    }

    // Getters
    public String getId() { return id; }
    public int getSuid() { return suid; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public int getSprite() { return sprite; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public ArrayList<Pokemon> getPokemonTeam() { return pokemonTeam; }
    public ArrayList<Pokemon> getInventory() { return inventory; }
    
    public double getWinRatio() {
        int total = wins + losses;
        if (total == 0) return 0;
        return (double) wins / total * 100;
    }

    // Setters
    public void setId(String id) { 
        this.id = id;
        try {
            this.suid = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            this.suid = -1;
        }
    }
    
    public void setSuid(int suid) {
        this.suid = suid;
        this.id = String.valueOf(suid);
    }
    
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setSprite(int sprite) { this.sprite = sprite; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setPokemonTeam(ArrayList<Pokemon> team) { this.pokemonTeam = team; }
    public void setInventory(ArrayList<Pokemon> inventory) { this.inventory = inventory; }
    
    public void addPokemonToTeam(Pokemon pokemon) {
        if (pokemonTeam.size() < 3) {
            pokemonTeam.add(pokemon);
        }
    }
    
    public void addPokemonToInventory(Pokemon pokemon) {
        inventory.add(pokemon);
    }
}