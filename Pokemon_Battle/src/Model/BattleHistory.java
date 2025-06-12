package Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Stack;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BattleHistory {
    
    // Atributos
    private final Stack<String> historyStack = new Stack<>(); // Pila para almacenar el historial de acciones
    private final String battleId; // ID único de la batalla
    private final String trainer1Name;
    private final String trainer2Name;
    private static final String HISTORY_DIRECTORY = "BattleHistories"; // Directorio para guardar los historiales de batalla
    private static final String GLOBAL_HISTORY_FILE = "MovementHistory.txt"; // Archivo para el historial global de movimientos

    // Constructor
    public BattleHistory(String trainer1Name, String trainer2Name) {
        this.trainer1Name = trainer1Name;
        this.trainer2Name = trainer2Name;
        this.battleId = generateBattleId();
        ensureDirectoryExists();
    }

    // Genera un ID único para la batalla 
    private String generateBattleId() {
        File historyDir = new File(HISTORY_DIRECTORY);
        if (!historyDir.exists() || historyDir.listFiles((dir, name) -> name.startsWith("Battle_1") && name.endsWith(".txt")).length == 0 || historyDir.listFiles((dir, name) -> name.startsWith("Battle_") && name.endsWith(".txt")).length == 0) {
            System.out.println("hola desde battle 1");
            return "Battle_1"; // Si es la primera batalla, asigna el ID Battle_1
        } else {
            System.out.println("hola desde battle 2");
            int lastBattleId = historyDir.listFiles((dir, name) -> name.startsWith("Battle_") && name.endsWith(".txt")).length; // Cuenta los archivos que cumplen con el patrón
            return "Battle_" + (lastBattleId + 1); // Incrementa el ID de la última batalla
        }
    }

    // Asegura que el directorio de historiales exista
    private void ensureDirectoryExists() {
        File directory = new File(HISTORY_DIRECTORY); // Crea el directorio si no existe
        if (!directory.exists()) { 
            directory.mkdir(); 
        }
    }

    // Métodos de registro
    public void logBattleStart() {
        addAction("INICIO: Batalla entre " + trainer1Name + " y " + trainer2Name);
    }

    // Registra el envío de un Pokémon por parte de un entrenador
    public void logPokemonSent(int trainer, String pokemonName) {
        addAction(getTrainerName(trainer) + " envía a " + pokemonName);
    }

    // Registra el cambio de Pokémon de un entrenador
    public void logPokemonChange(int trainer, String oldPokemon, String newPokemon) {
        addAction(getTrainerName(trainer) + " cambia de " + oldPokemon + " a " + newPokemon);
    }

    // Registra la selección de un movimiento por parte de un Pokémon
    public void logMoveSelection(int trainer, String pokemonName, String moveName) {
        addAction(getTrainerName(trainer) + " - " + pokemonName + " usa " + moveName);
    }

    // Registra un ataque realizado por un Pokémon
    public void logAttack(String attacker, String move, String defender, int damage, int remainingHP) {
        addAction(String.format("ATAQUE: %s usa %s contra %s - Daño: %d - HP restante: %d", 
                attacker, move, defender, damage, remainingHP));
    }

    // Registra que un Pokémon se ha debilitado
    public void logPokemonFainted(String pokemonName) {
        addAction("¡" + pokemonName + " se ha debilitado!");
    }

    // Registra el final de la batalla y quién ha ganado
    public void logBattleEnd(String winnerName) {
        addAction("FIN: ¡" + winnerName + " gana la batalla!");
    }

    // Método para obtener el nombre del entrenador según su índice
    private String getTrainerName(int trainer) {
        return trainer == 0 ? trainer1Name : trainer2Name;
    }

    // Método para agregar una acción al historial 
    private void addAction(String action) {
        String logEntry = action;
        historyStack.push(logEntry);
    }

    // Métodos para obtener el historial
    public String getHistoryAsText() {
        StringBuilder sb = new StringBuilder();
        sb.append("════════ HISTORIAL DE BATALLA ════════\n");
        sb.append("Entrenadores: ").append(trainer1Name).append(" vs ").append(trainer2Name).append("\n");
        sb.append("ID Batalla: ").append(battleId).append("\n\n");
        for (String action : historyStack) {
            sb.append(action).append("\n");
        }
        
        return sb.toString();
    }

    // Métodos para guardar el historial
    public void saveBattleHistory() {
        saveIndividualBattleFile();
    }

    // Guarda el historial de la batalla en un archivo individual y en el historial global
    private void saveIndividualBattleFile() {
        String filename = HISTORY_DIRECTORY + File.separator + battleId + ".txt";
        saveToFile(filename, getHistoryAsText());
    }

    // Agrega el historial de la batalla al historial global de movimientos

    // Método auxiliar para guardar el historial en un archivo
    private void saveToFile(String filename, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(content);
        } catch (IOException e) {
            System.err.println("Error al guardar el historial: " + e.getMessage());
        }
    }

    

}