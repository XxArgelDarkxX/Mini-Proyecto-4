package Model;

import java.util.ArrayList;
import java.util.Scanner;

public class Trainer {

    private String name;

    private ArrayList<Pokemon> pokemonTeam = new ArrayList<>();
    public Trainer() {
    }

    public Trainer(String name, ArrayList<Pokemon> pokemonTeam) {
        this.name = name;
        this.pokemonTeam = pokemonTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Pokemon> getPokemonTeam() {
        return pokemonTeam;
    }

    public void setPokemonTeam(ArrayList<Pokemon> pokemonTeam) {
        this.pokemonTeam = pokemonTeam;
    }

    public void setRandomPokemonTeam(ArrayList<Pokemon> pokemons) {
        // Se crea un equipo de 3 Pokemon aleatorios.
        for (int i = 0; i < 3; i++) {
            int randomIndex = (int) (Math.random() * pokemons.size());
            this.pokemonTeam.add(pokemons.get(randomIndex));
            pokemons.remove(randomIndex);
        }
    }

    // Metodo para elegir pokemons para la batalla. (no se puede cambiar en medio de esta)
    public void choosePokemon(Pokemon[] pokemonsBattle, Trainer[] trainers, Scanner scanner) {
        // Primer pokemon para la batalla
        if(pokemonsBattle[0] == null && pokemonsBattle[1] == null) {
            for(int i = 0; i < trainers.length; i++) {
                Trainer trainer = trainers[i];
                System.out.println("Entrenador: " + trainer.getName() + " elija su pokemon inicial: ");
                for(int j = 0 ; j < trainer.pokemonTeam.size(); j++){
                    System.out.println((j+1)+ " " + trainer.pokemonTeam.get(j).getName() + " " + trainer.pokemonTeam.get(j).getHp());
                }
                int opcion;
                do{
                    System.out.print("\nIngrese el numero del pokemon (1 -" + trainer.getPokemonTeam().size() + "): ");
                    while(!scanner.hasNextInt()){
                        System.out.println("Debe ser un numero");
                        scanner.next();
                    }
                    opcion = scanner.nextInt();
                }while(opcion > trainer.pokemonTeam.size() || opcion < 1);
                scanner.nextLine();
                pokemonsBattle[i] = trainer.pokemonTeam.get(opcion -1 );
            }
        // Si el pokemon ya fue derrotado, se da la posibilidad de elegir otro de los restantes.
        }else if (pokemonsBattle[1] == null || pokemonsBattle[0] == null) {
            int index = (pokemonsBattle[0] == null) ? 0 : 1;
            int option;
            Trainer trainer = trainers[index];
            System.out.println("Cambiar Pokémon: ");
            for (int i = 0; i < pokemonTeam.size(); i++) {
                System.out.println("\t" + (i + 1) + ". " + trainer.pokemonTeam.get(i).getName() + " (HP: " + trainer.pokemonTeam.get(i).getHp() + ")");
            }
        
            do {
                System.out.println(trainer.getName() + ", elige un Pokémon (1 - " + pokemonTeam.size() + "):");
                try {
                    option = scanner.nextInt();
                    if (option < 1 || option > pokemonTeam.size()) {
                        System.out.println("Opción fuera de rango. Intenta nuevamente.");
                    } else {
                        break; // opción válida
                    }
                } catch (Exception e) {
                    System.out.println("Entrada inválida. Ingresa un número.");
                    scanner.next(); // limpiar entrada inválida
                }
            } while (true);
            pokemonsBattle[index] = pokemonTeam.get(option - 1);
        }
        
    }


}