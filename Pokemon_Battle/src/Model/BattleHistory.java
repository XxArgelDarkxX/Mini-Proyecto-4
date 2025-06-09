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
    private final Stack<String> historyStack = new Stack<>();
    private final String battleId;
    private final String trainer1Name;
    private final String trainer2Name;
    private static final String HISTORY_DIRECTORY = "BattleHistories";
    private static final String GLOBAL_HISTORY_FILE = "MovementHistory.txt";

    public BattleHistory(String trainer1Name, String trainer2Name) {
        this.trainer1Name = trainer1Name;
        this.trainer2Name = trainer2Name;
        this.battleId = generateBattleId();
        ensureDirectoryExists();
    }

    private String generateBattleId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return "Battle_" + LocalDateTime.now().format(formatter);
    }

    private void ensureDirectoryExists() {
        File directory = new File(HISTORY_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    // Métodos de registro
    public void logBattleStart() {
        addAction("INICIO: Batalla entre " + trainer1Name + " y " + trainer2Name);
    }

    public void logPokemonSent(int trainer, String pokemonName) {
        addAction(getTrainerName(trainer) + " envía a " + pokemonName);
    }

    public void logPokemonChange(int trainer, String oldPokemon, String newPokemon) {
        addAction(getTrainerName(trainer) + " cambia de " + oldPokemon + " a " + newPokemon);
    }

    public void logMoveSelection(int trainer, String pokemonName, String moveName) {
        addAction(getTrainerName(trainer) + " - " + pokemonName + " usa " + moveName);
    }

    public void logAttack(String attacker, String move, String defender, int damage, int remainingHP) {
        addAction(String.format("ATAQUE: %s usa %s contra %s - Daño: %d - HP restante: %d", 
                attacker, move, defender, damage, remainingHP));
    }

    public void logPokemonFainted(String pokemonName) {
        addAction("¡" + pokemonName + " se ha debilitado!");
    }

    public void logBattleEnd(String winnerName) {
        addAction("FIN: ¡" + winnerName + " gana la batalla!");
    }

    private String getTrainerName(int trainer) {
        return trainer == 0 ? trainer1Name : trainer2Name;
    }

    private void addAction(String action) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logEntry = "[" + timestamp + "] " + action;
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
        appendToGlobalHistory();
    }

    private void saveIndividualBattleFile() {
        String filename = HISTORY_DIRECTORY + File.separator + battleId + ".txt";
        saveToFile(filename, getHistoryAsText());
    }

    private void appendToGlobalHistory() {
        try (FileWriter fw = new FileWriter(HISTORY_DIRECTORY + File.separator + GLOBAL_HISTORY_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
             
            out.println("\n════════ " + battleId + " ════════");
            out.println("Entrenadores: " + trainer1Name + " vs " + trainer2Name);
            out.println("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            out.println();
            
            for (String action : historyStack) {
                out.println(action);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el historial global: " + e.getMessage());
        }
    }

    private void saveToFile(String filename, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(content);
        } catch (IOException e) {
            System.err.println("Error al guardar el historial: " + e.getMessage());
        }
    }

    public void clearHistory() {
        historyStack.clear();
    }
}