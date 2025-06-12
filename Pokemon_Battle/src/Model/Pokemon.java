package Model;
import Model.Data.Data;
import java.util.ArrayList;
public class Pokemon {
    private String pokedexId;
    private String name;
    private Type type;
    private short hp;
    private short level = 50; // Nivel por defecto de los Pokemon
    private short attack;
    private short specialAttack;
    private short defense;
    private short specialDefense;
    private short speed;
    private ArrayList<Movement> moves;
    private String frontSprite;
    private String backSprite;
    private String icon;
    private static final double[][] TYPE_EFFECTIVENESS = {
        //FUE   AGU   PLA   TIE   ELE   VOL   PSI   HIE   BIC   ROC   FAN   DRA   LUC   HAD   SIN   NOR   ACE   VEN
        { 0.5,  0.5,  2.0,  1.0,  1.0,  1.0,  1.0,  2.0,  2.0,  0.5,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0 }, // FUEGO
        { 2.0,  0.5,  0.5,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0 }, // AGUA
        { 0.5,  2.0,  0.5,  2.0,  1.0,  0.5,  1.0,  1.0,  0.5,  2.0,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  0.5,  0.5 }, // PLANTA
        { 2.0,  1.0,  0.5,  1.0,  2.0,  0.0,  1.0,  1.0,  0.5,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  2.0 }, // TIERRA
        { 1.0,  2.0,  0.5,  0.0,  0.5,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0 }, // ELECTRICO
        { 1.0,  1.0,  2.0,  1.0,  0.5,  1.0,  1.0,  1.0,  2.0,  0.5,  1.0,  1.0,  2.0,  1.0,  1.0,  1.0,  0.5,  1.0 }, // VOLADOR
        { 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  0.0,  1.0,  0.5,  2.0 }, // PSIQUICO
        { 0.5,  0.5,  2.0,  2.0,  1.0,  2.0,  1.0,  0.5,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0 }, // HIELO
        { 0.5,  1.0,  2.0,  1.0,  1.0,  0.5,  2.0,  1.0,  1.0,  1.0,  0.5,  1.0,  0.5,  0.5,  2.0,  1.0,  0.5,  0.5 }, // BICHO
        { 2.0,  1.0,  1.0,  0.5,  1.0,  2.0,  1.0,  2.0,  2.0,  1.0,  1.0,  1.0,  0.5,  1.0,  1.0,  1.0,  0.5,  1.0 }, // ROCA
        { 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  0.5,  0.5,  0.0,  1.0,  1.0 }, // FANTASMA
        { 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  0.0,  1.0,  1.0,  0.5,  1.0 }, // DRAGON
        { 1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  0.5,  2.0,  0.5,  2.0,  0.0,  1.0,  1.0,  0.5,  2.0,  2.0,  2.0,  0.5 }, // LUCHA
        { 0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  2.0,  1.0,  2.0,  1.0,  0.5,  0.5 }, // HADA
        { 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  1.0,  2.0,  1.0,  0.5,  0.5,  0.5,  1.0,  1.0,  1.0 }, // SINIESTRO
        { 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  0.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0 }, // NORMAL
        { 0.5,  0.5,  1.0,  1.0,  0.5,  1.0,  1.0,  2.0,  1.0,  2.0,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  0.5,  1.0 }, // ACERO
        { 1.0,  1.0,  2.0,  0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  0.5,  1.0,  1.0,  2.0,  1.0,  1.0,  0.0,  0.5 }  // VENENO
    };


    public Pokemon(){}

    // Constructor de la clase Pokemon.
    public Pokemon(String pokedexId, String name, Type type, short baseHp, short baseAttack, short baseSpecialAttack, short baseDefense,
            short baseSpecialDefense, short baseSpeed, ArrayList<Movement> moves) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.type = type;
        this.hp = calculateHP(baseHp, level);
        this.attack = calculateStats(baseAttack, level);
        this.specialAttack = calculateStats(baseSpecialAttack, level);
        this.defense = calculateStats(baseDefense, level);
        this.specialDefense = calculateStats(baseSpecialDefense, level);
        this.speed = calculateStats(baseSpeed, level);
        this.moves = moves;

        this.frontSprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokedexId + ".png";
        this.backSprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/" + pokedexId + ".png";
        this.icon = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/" + pokedexId + ".png";
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public short getHp() {
        return hp;
    }
    
    public void setHp(short hp) {
        this.hp = hp;
    }
    
    public short getLevel() {
        return level;
    }
    
    public void setLevel(short level) {
        this.level = level;
    }
    
    public short getAttack() {
        return attack;
    }

    public void setAttack(short attack) {
        this.attack = attack;
    }
    
    public short getSpecialAttack() {
        return specialAttack;
    }
    
    public void setSpecialAttack(short specialAttack) {
        this.specialAttack = specialAttack;
    }
    
    public short getDefense() {
        return defense;
    }
    
    public void setDefense(short defense) {
        this.defense = defense;
    }
    
    public short getSpecialDefense() {
        return specialDefense;
    }
    
    public void setSpecialDefense(short specialDefense) {
        this.specialDefense = specialDefense;
    }
    
    public short getSpeed() {
        return speed;
    }
    
    public void setSpeed(short speed) {
        this.speed = speed;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }

    public String getPokedexId() {
        return pokedexId;
    }

    public String getFrontSprite() {
        return frontSprite;
    }

    public String getBackSprite() {
        return backSprite;
    }

    public String getIcon() {
        return icon;
    }

    public ArrayList<Movement> getMoves() {
        return moves;
    }
    
    public void setMoves(ArrayList<Movement> moves) {
        this.moves = moves;
    }

    public void setRandomMoves(ArrayList<Movement> moves) {
        this.moves = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int randomIndex = (int) (Math.random() * moves.size());
            this.moves.add(moves.get(randomIndex));
            moves.remove(randomIndex); // Se elimina el movimiento para evitar duplicados.
        }
        System.out.println("Los movimientos de " + this.name + " son: ");
        for (Movement move : this.moves) {
            System.out.println(move.getName() + " - " + move.getPower() + " de poder base.");
        }
        Data.initializeMoves(); // Se inicializan los movimientos para el siguiente Pokemon.
    }
    
    // Este método calcula la efectividad de un movimiento de un tipo de Pokemon sobre otro tipo de Pokemon.
    // El metodo es privado porque solo va a ser usado por la clase Pokemon.
    double moveEffectiveness(Type attack, Type defender) {
        return (double) TYPE_EFFECTIVENESS[attack.ordinal()][defender.ordinal()];
    }
    
    private short calculateStats(short baseStat, short level) {
        return (short) ((0.01*(2*baseStat+31+(0.25*85))*level) + 5); // Formula de stats
    }

    private short calculateHP(short baseStat, short level) {
        return (short) ((0.01*(2*baseStat+31+(0.25*85))*level) + level + 10); // Formula de HP
    }

    // Este método permite a un Pokemon atacar a otro Pokemon.
    public void movement(Pokemon target, int moveIndex) {
        Movement move = (Movement) this.moves.get(moveIndex);
        double effectiveness = moveEffectiveness(move.getType(), target.getType());
        // Se obtiene el ataque del Pokemon atacante.
        short currentAttack = move.getTypeAttack() == Movement.TypeAttack.FISICO ? this.attack : this.specialAttack; 
        short currentDefense = move.getTypeAttack() == Movement.TypeAttack.FISICO ? target.getDefense() 
        : target.getSpecialDefense(); // Se obtiene la defensa del Pokemon defensor.
        target.setHp((short) (target.getHp() - (((currentAttack/currentDefense) * move.getPower()) * effectiveness + 2))); // Se resta la vida del Pokemon objetivo.
    }

    public int executeMove(Pokemon defender, byte moveIndex) {
    Movement move = this.moves.get(moveIndex);
    double effectiveness = moveEffectiveness(move.getType(), defender.getType());
    
    short attackStat = move.getTypeAttack() == Movement.TypeAttack.FISICO ? this.attack : this.specialAttack;
    short defenseStat = move.getTypeAttack() == Movement.TypeAttack.FISICO ? defender.getDefense() : defender.getSpecialDefense();
    
    // Fórmula simplificada de daño Pokémon
    int damage = (int) ((((2 * this.level / 5 + 2) * move.getPower() * attackStat / defenseStat) / 50 + 2) * effectiveness);
    
    defender.setHp((short) Math.max(0, defender.getHp() - damage));
    return damage;
    }

    public String getMoveName(byte moveIndex) {
        return moves.get(moveIndex).getName();
    }
}
