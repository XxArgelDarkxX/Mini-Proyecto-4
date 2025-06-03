package Model.Data;

import Model.Movement;
import Model.Pokemon;
import Model.Type;
import java.util.ArrayList;

public class Data {
    private static final ArrayList<Movement> fireMoves = new ArrayList<>();
    private static final ArrayList<Movement> waterMoves = new ArrayList<>();
    private static final ArrayList<Movement> grassMoves = new ArrayList<>();
    private static final ArrayList<Movement> groundMoves = new ArrayList<>();
    private static final ArrayList<Movement> electricMoves = new ArrayList<>();
    private static final ArrayList<Movement> flyingMoves = new ArrayList<>();
    private static final ArrayList<Movement> psychicMoves = new ArrayList<>();
    private static final ArrayList<Movement> iceMoves = new ArrayList<>();
    private static final ArrayList<Movement> bugMoves = new ArrayList<>();
    private static final ArrayList<Movement> rockMoves = new ArrayList<>();
    private static final ArrayList<Movement> ghostMoves = new ArrayList<>();
    private static final ArrayList<Movement> dragonMoves = new ArrayList<>();
    private static final ArrayList<Movement> fightingMoves = new ArrayList<>();
    private static final ArrayList<Movement> fairyMoves = new ArrayList<>();
    private static final ArrayList<Movement> darkMoves = new ArrayList<>();
    private static final ArrayList<Movement> normalMoves = new ArrayList<>();
    private static final ArrayList<Movement> steelMoves = new ArrayList<>();
    private static final ArrayList<Movement> poisonMoves = new ArrayList<>();
    private static final ArrayList<Pokemon> pokemons = new ArrayList<>();

    //data de los movimientos
    public static void initializeMoves() {
        // Movimientos tipo Fuego
        fireMoves.clear();
        fireMoves.add(new Movement("Lanzallamas", "FISICO", "FUEGO", (byte)90));
        fireMoves.add(new Movement("Llamarada", "ESPECIAL", "FUEGO", (byte)85));
        fireMoves.add(new Movement("Puño Fuego", "FISICO", "FUEGO", (byte)75));
        fireMoves.add(new Movement("Giro Fuego", "ESPECIAL", "FUEGO", (byte)70));
        fireMoves.add(new Movement("Nitrocarga", "FISICO", "FUEGO", (byte)65));

        // Movimientos tipo Agua
        waterMoves.clear();
        waterMoves.add(new Movement("Hidrobomba", "ESPECIAL", "AGUA", (byte)95));
        waterMoves.add(new Movement("Surf", "ESPECIAL", "AGUA", (byte)85));
        waterMoves.add(new Movement("Acua Cola", "FISICO", "AGUA", (byte)80));
        waterMoves.add(new Movement("Pistola Agua", "ESPECIAL", "AGUA", (byte)65));
        waterMoves.add(new Movement("Cascada", "FISICO", "AGUA", (byte)75));

        // Movimientos tipo Planta
        grassMoves.clear();
        grassMoves.add(new Movement("Rayo Solar", "ESPECIAL", "PLANTA", (byte)95));
        grassMoves.add(new Movement("Hoja Afilada", "FISICO", "PLANTA", (byte)85));
        grassMoves.add(new Movement("Latigazo", "FISICO", "PLANTA", (byte)80));
        grassMoves.add(new Movement("Bomba Germen", "ESPECIAL", "PLANTA", (byte)75));
        grassMoves.add(new Movement("Drenadoras", "ESPECIAL", "PLANTA", (byte)70));

        // Movimientos tipo Tierra
        groundMoves.clear();
        groundMoves.add(new Movement("Terremoto", "FISICO", "TIERRA", (byte)95));
        groundMoves.add(new Movement("Excavar", "FISICO", "TIERRA", (byte)85));
        groundMoves.add(new Movement("Tierra Viva", "ESPECIAL", "TIERRA", (byte)80));
        groundMoves.add(new Movement("Bofetón Lodo", "ESPECIAL", "TIERRA", (byte)75));
        groundMoves.add(new Movement("Avalancha", "FISICO", "TIERRA", (byte)70));
        
        // Movimientos tipo Eléctrico
        electricMoves.clear();
        electricMoves.add(new Movement("Trueno", "ESPECIAL", "ELECTRICO", (byte)95));
        electricMoves.add(new Movement("Rayo", "ESPECIAL", "ELECTRICO", (byte)90));
        electricMoves.add(new Movement("Chispa", "FISICO", "ELECTRICO", (byte)75));
        electricMoves.add(new Movement("Voltio Cruel", "FISICO", "ELECTRICO", (byte)85));
        electricMoves.add(new Movement("Impactrueno", "ESPECIAL", "ELECTRICO", (byte)65));

        // Movimientos tipo Volador
        flyingMoves.clear();
        flyingMoves.add(new Movement("Pájaro Osado", "FISICO", "VOLADOR", (byte)95));
        flyingMoves.add(new Movement("Ataque Aéreo", "FISICO", "VOLADOR", (byte)85));
        flyingMoves.add(new Movement("Aire Afilado", "ESPECIAL", "VOLADOR", (byte)75));
        flyingMoves.add(new Movement("Tajo Aéreo", "ESPECIAL", "VOLADOR", (byte)80));
        flyingMoves.add(new Movement("Tornado", "ESPECIAL", "VOLADOR", (byte)65));

        // Movimientos tipo Psíquico
        psychicMoves.clear();
        psychicMoves.add(new Movement("Psíquico", "ESPECIAL", "PSIQUICO", (byte)90));
        psychicMoves.add(new Movement("Psicorrayo", "ESPECIAL", "PSIQUICO", (byte)85));
        psychicMoves.add(new Movement("Confusión", "ESPECIAL", "PSIQUICO", (byte)70));
        psychicMoves.add(new Movement("Psicocorte", "FISICO", "PSIQUICO", (byte)80));
        psychicMoves.add(new Movement("Cabezazo Zen", "FISICO", "PSIQUICO", (byte)75));

        // Movimientos tipo Hielo
        iceMoves.clear();
        iceMoves.add(new Movement("Rayo Hielo", "ESPECIAL", "HIELO", (byte)90));
        iceMoves.add(new Movement("Ventisca", "ESPECIAL", "HIELO", (byte)95));
        iceMoves.add(new Movement("Puño Hielo", "FISICO", "HIELO", (byte)75));
        iceMoves.add(new Movement("Colmillo Hielo", "FISICO", "HIELO", (byte)80));
        iceMoves.add(new Movement("Canto Helado", "FISICO", "HIELO", (byte)70));

        // Movimientos tipo Bicho
        bugMoves.clear();
        bugMoves.add(new Movement("Zumbido", "ESPECIAL", "BICHO", (byte)90));
        bugMoves.add(new Movement("Tijera X", "FISICO", "BICHO", (byte)85));
        bugMoves.add(new Movement("Chupavidas", "FISICO", "BICHO", (byte)80));
        bugMoves.add(new Movement("Pin Misil", "FISICO", "BICHO", (byte)75));
        bugMoves.add(new Movement("Picadura", "FISICO", "BICHO", (byte)65));

        // Movimientos tipo Roca
        rockMoves.clear();
        rockMoves.add(new Movement("Roca Afilada", "FISICO", "ROCA", (byte)95));
        rockMoves.add(new Movement("Lanzarrocas", "FISICO", "ROCA", (byte)85));
        rockMoves.add(new Movement("Pedrada", "FISICO", "ROCA", (byte)75));
        rockMoves.add(new Movement("Avalancha", "FISICO", "ROCA", (byte)80));
        rockMoves.add(new Movement("Tumba Rocas", "FISICO", "ROCA", (byte)70));

        // Movimientos tipo Fantasma
        ghostMoves.clear();
        ghostMoves.add(new Movement("Bola Sombra", "ESPECIAL", "FANTASMA", (byte)90));
        ghostMoves.add(new Movement("Golpe Umbrío", "FISICO", "FANTASMA", (byte)85));
        ghostMoves.add(new Movement("Garra Umbría", "FISICO", "FANTASMA", (byte)80));
        ghostMoves.add(new Movement("Tinieblas", "ESPECIAL", "FANTASMA", (byte)75));
        ghostMoves.add(new Movement("Rencor", "ESPECIAL", "FANTASMA", (byte)70));

        // Movimientos tipo Dragón
        dragonMoves.clear();
        dragonMoves.add(new Movement("Enfado", "FISICO", "DRAGON", (byte)95));
        dragonMoves.add(new Movement("Pulso Dragón", "ESPECIAL", "DRAGON", (byte)90));
        dragonMoves.add(new Movement("Garra Dragón", "FISICO", "DRAGON", (byte)85));
        dragonMoves.add(new Movement("Cola Dragón", "FISICO", "DRAGON", (byte)80));
        dragonMoves.add(new Movement("Aliento Dragón", "ESPECIAL", "DRAGON", (byte)75));

        // Movimientos tipo Lucha
        fightingMoves.clear();
        fightingMoves.add(new Movement("A Bocajarro", "FISICO", "LUCHA", (byte)95));
        fightingMoves.add(new Movement("Patada Salto Alta", "FISICO", "LUCHA", (byte)90));
        fightingMoves.add(new Movement("Tajo Cruzado", "FISICO", "LUCHA", (byte)85));
        fightingMoves.add(new Movement("Golpe Kárate", "FISICO", "LUCHA", (byte)75));
        fightingMoves.add(new Movement("Contraataque", "FISICO", "LUCHA", (byte)70));

        // Movimientos tipo Hada
        fairyMoves.clear();
        fairyMoves.add(new Movement("Fuerza Lunar", "ESPECIAL", "HADA", (byte)95));
        fairyMoves.add(new Movement("Brillo Mágico", "ESPECIAL", "HADA", (byte)85));
        fairyMoves.add(new Movement("Beso Drenaje", "ESPECIAL", "HADA", (byte)80));
        fairyMoves.add(new Movement("Voz Cautivadora", "ESPECIAL", "HADA", (byte)75));
        fairyMoves.add(new Movement("Carantoña", "FISICO", "HADA", (byte)70));

        // Movimientos tipo Siniestro
        darkMoves.clear();
        darkMoves.add(new Movement("Pulso Umbrío", "ESPECIAL", "SINIESTRO", (byte)90));
        darkMoves.add(new Movement("Triturar", "FISICO", "SINIESTRO", (byte)85));
        darkMoves.add(new Movement("Mordisco", "FISICO", "SINIESTRO", (byte)75));
        darkMoves.add(new Movement("Finta", "FISICO", "SINIESTRO", (byte)70));
        darkMoves.add(new Movement("Persecución", "FISICO", "SINIESTRO", (byte)65));

        // Movimientos tipo Normal
        normalMoves.clear();
        normalMoves.add(new Movement("Hiperrayo", "ESPECIAL", "NORMAL", (byte)95));
        normalMoves.add(new Movement("Golpe Cuerpo", "FISICO", "NORMAL", (byte)85));
        normalMoves.add(new Movement("Placaje", "FISICO", "NORMAL", (byte)65));
        normalMoves.add(new Movement("Doble Filo", "FISICO", "NORMAL", (byte)90));
        normalMoves.add(new Movement("Ataque Rápido", "FISICO", "NORMAL", (byte)70));

        // Movimientos tipo Acero
        steelMoves.clear();
        steelMoves.add(new Movement("Cabeza de Hierro", "FISICO", "ACERO", (byte)95));
        steelMoves.add(new Movement("Garra Metal", "FISICO", "ACERO", (byte)85));
        steelMoves.add(new Movement("Cola Férrea", "FISICO", "ACERO", (byte)80));
        steelMoves.add(new Movement("Bomba Imán", "ESPECIAL", "ACERO", (byte)75));
        steelMoves.add(new Movement("Púas Acero", "FISICO", "ACERO", (byte)70));

        // Movimientos tipo Veneno
        poisonMoves.clear();
        poisonMoves.add(new Movement("Bomba Lodo", "ESPECIAL", "VENENO", (byte)90));
        poisonMoves.add(new Movement("Residuos", "ESPECIAL", "VENENO", (byte)85));
        poisonMoves.add(new Movement("Cola Veneno", "FISICO", "VENENO", (byte)80));
        poisonMoves.add(new Movement("Púas Tóxicas", "FISICO", "VENENO", (byte)75));
        poisonMoves.add(new Movement("Ácido", "ESPECIAL", "VENENO", (byte)70));
    }

    public static void initializePokemons() {
        // Pokémon tipo Fuego
        pokemons.add(new Pokemon("6", "Charizard", Type.FUEGO, (short)78, (short)84, (short)109, (short)78, (short)85, (short)100, new ArrayList<>(fireMoves.subList(0, 4))));
        pokemons.add(new Pokemon("59", "Arcanine", Type.FUEGO, (short)90, (short)110, (short)100, (short)80, (short)80, (short)95, new ArrayList<>(fireMoves.subList(1, 5))));
        pokemons.add(new Pokemon("126", "Magmar", Type.FUEGO, (short)65, (short)95, (short)100, (short)57, (short)85, (short)93, new ArrayList<>(fireMoves.subList(0, 4))));
        pokemons.add(new Pokemon("136", "Flareon", Type.FUEGO, (short)65, (short)130, (short)95, (short)60, (short)110, (short)65, new ArrayList<>(fireMoves.subList(1, 5))));
        pokemons.add(new Pokemon("146", "Moltres", Type.FUEGO, (short)90, (short)100, (short)125, (short)90, (short)85, (short)90, new ArrayList<>(fireMoves.subList(0, 4))));
    
        // Pokémon tipo Agua
        pokemons.add(new Pokemon("9", "Blastoise", Type.AGUA, (short)79, (short)83, (short)85, (short)100, (short)105, (short)78, new ArrayList<>(waterMoves.subList(0, 4))));
        pokemons.add(new Pokemon("130", "Gyarados", Type.AGUA, (short)95, (short)125, (short)60, (short)79, (short)100, (short)81, new ArrayList<>(waterMoves.subList(1, 5))));
        pokemons.add(new Pokemon("134", "Vaporeon", Type.AGUA, (short)130, (short)65, (short)110, (short)60, (short)95, (short)65, new ArrayList<>(waterMoves.subList(0, 4))));
        pokemons.add(new Pokemon("121", "Starmie", Type.AGUA, (short)60, (short)75, (short)100, (short)85, (short)85, (short)115, new ArrayList<>(waterMoves.subList(1, 5))));
        pokemons.add(new Pokemon("91", "Cloyster", Type.AGUA, (short)50, (short)95, (short)85, (short)180, (short)45, (short)70, new ArrayList<>(waterMoves.subList(0, 4))));
    
        // Pokémon tipo Planta
        pokemons.add(new Pokemon("3", "Venusaur", Type.PLANTA, (short)80, (short)82, (short)100, (short)83, (short)100, (short)80, new ArrayList<>(grassMoves.subList(0, 4))));
        pokemons.add(new Pokemon("71", "Victreebel", Type.PLANTA, (short)80, (short)105, (short)100, (short)65, (short)70, (short)70, new ArrayList<>(grassMoves.subList(1, 5))));
        pokemons.add(new Pokemon("45", "Vileplume", Type.PLANTA, (short)75, (short)80, (short)100, (short)85, (short)90, (short)50, new ArrayList<>(grassMoves.subList(0, 4))));
        pokemons.add(new Pokemon("103", "Exeggutor", Type.PLANTA, (short)95, (short)95, (short)125, (short)85, (short)65, (short)55, new ArrayList<>(grassMoves.subList(1, 5))));
        pokemons.add(new Pokemon("114", "Tangela", Type.PLANTA, (short)65, (short)55, (short)100, (short)115, (short)40, (short)60, new ArrayList<>(grassMoves.subList(0, 4))));
    
        // Pokémon tipo Eléctrico
        pokemons.add(new Pokemon("26", "Raichu", Type.ELECTRICO, (short)60, (short)90, (short)90, (short)55, (short)80, (short)110, new ArrayList<>(electricMoves.subList(0, 4))));
        pokemons.add(new Pokemon("135", "Jolteon", Type.ELECTRICO, (short)65, (short)65, (short)110, (short)60, (short)95, (short)130, new ArrayList<>(electricMoves.subList(1, 5))));
        pokemons.add(new Pokemon("125", "Electabuzz", Type.ELECTRICO, (short)65, (short)83, (short)95, (short)57, (short)85, (short)105, new ArrayList<>(electricMoves.subList(0, 4))));
        pokemons.add(new Pokemon("145", "Zapdos", Type.ELECTRICO, (short)90, (short)90, (short)125, (short)85, (short)90, (short)100, new ArrayList<>(electricMoves.subList(1, 5))));
        pokemons.add(new Pokemon("82", "Magneton", Type.ELECTRICO, (short)50, (short)60, (short)120, (short)95, (short)70, (short)70, new ArrayList<>(electricMoves.subList(0, 4))));
    
        // Pokémon tipo Volador
        pokemons.add(new Pokemon("18", "Pidgeot", Type.VOLADOR, (short)83, (short)80, (short)70, (short)75, (short)70, (short)101, new ArrayList<>(flyingMoves.subList(0, 4))));
        pokemons.add(new Pokemon("22", "Fearow", Type.VOLADOR, (short)65, (short)90, (short)61, (short)65, (short)61, (short)100, new ArrayList<>(flyingMoves.subList(1, 5))));
        pokemons.add(new Pokemon("142", "Aerodactyl", Type.VOLADOR, (short)80, (short)105, (short)60, (short)65, (short)75, (short)130, new ArrayList<>(flyingMoves.subList(0, 4))));
        pokemons.add(new Pokemon("123", "Scyther", Type.VOLADOR, (short)70, (short)110, (short)55, (short)80, (short)80, (short)105, new ArrayList<>(flyingMoves.subList(1, 5))));
        pokemons.add(new Pokemon("85", "Dodrio", Type.VOLADOR, (short)60, (short)110, (short)60, (short)70, (short)60, (short)110, new ArrayList<>(flyingMoves.subList(0, 4))));
    
        // Pokémon tipo Psíquico
        pokemons.add(new Pokemon("65", "Alakazam", Type.PSIQUICO, (short)55, (short)50, (short)135, (short)45, (short)95, (short)120, new ArrayList<>(psychicMoves.subList(0, 4))));
        pokemons.add(new Pokemon("80", "Slowbro", Type.PSIQUICO, (short)95, (short)75, (short)100, (short)110, (short)80, (short)30, new ArrayList<>(psychicMoves.subList(1, 5))));
        pokemons.add(new Pokemon("97", "Hypno", Type.PSIQUICO, (short)85, (short)73, (short)73, (short)70, (short)115, (short)67, new ArrayList<>(psychicMoves.subList(0, 4))));
        pokemons.add(new Pokemon("150", "Mewtwo", Type.PSIQUICO, (short)106, (short)110, (short)154, (short)90, (short)90, (short)130, new ArrayList<>(psychicMoves.subList(1, 5))));
        pokemons.add(new Pokemon("151", "Mew", Type.PSIQUICO, (short)100, (short)100, (short)100, (short)100, (short)100, (short)100, new ArrayList<>(psychicMoves.subList(0, 4))));
    
        // Pokémon tipo Hielo
        pokemons.add(new Pokemon("144", "Articuno", Type.HIELO, (short)90, (short)85, (short)95, (short)100, (short)125, (short)85, new ArrayList<>(iceMoves.subList(0, 4))));
        pokemons.add(new Pokemon("124", "Jynx", Type.HIELO, (short)65, (short)50, (short)115, (short)35, (short)95, (short)95, new ArrayList<>(iceMoves.subList(1, 5))));
        pokemons.add(new Pokemon("87", "Dewgong", Type.HIELO, (short)90, (short)70, (short)70, (short)80, (short)95, (short)70, new ArrayList<>(iceMoves.subList(0, 4))));
        pokemons.add(new Pokemon("91", "Cloyster", Type.HIELO, (short)50, (short)95, (short)85, (short)180, (short)45, (short)70, new ArrayList<>(iceMoves.subList(1, 5))));
        pokemons.add(new Pokemon("131", "Lapras", Type.HIELO, (short)130, (short)85, (short)85, (short)80, (short)95, (short)60, new ArrayList<>(iceMoves.subList(0, 4))));
    
        // Pokémon tipo Bicho
        pokemons.add(new Pokemon("12", "Butterfree", Type.BICHO, (short)60, (short)45, (short)90, (short)50, (short)80, (short)70, new ArrayList<>(bugMoves.subList(0, 4))));
        pokemons.add(new Pokemon("15", "Beedrill", Type.BICHO, (short)65, (short)90, (short)45, (short)40, (short)80, (short)75, new ArrayList<>(bugMoves.subList(1, 5))));
        pokemons.add(new Pokemon("127", "Pinsir", Type.BICHO, (short)65, (short)125, (short)55, (short)100, (short)70, (short)85, new ArrayList<>(bugMoves.subList(0, 4))));
        pokemons.add(new Pokemon("123", "Scyther", Type.BICHO, (short)70, (short)110, (short)55, (short)80, (short)80, (short)105, new ArrayList<>(bugMoves.subList(1, 5))));
        pokemons.add(new Pokemon("47", "Parasect", Type.BICHO, (short)60, (short)95, (short)60, (short)80, (short)80, (short)30, new ArrayList<>(bugMoves.subList(0, 4))));
    
        // Pokémon tipo Roca
        pokemons.add(new Pokemon("76", "Golem", Type.ROCA, (short)80, (short)120, (short)55, (short)130, (short)65, (short)45, new ArrayList<>(rockMoves.subList(0, 4))));
        pokemons.add(new Pokemon("112", "Rhydon", Type.ROCA, (short)105, (short)130, (short)45, (short)120, (short)45, (short)40, new ArrayList<>(rockMoves.subList(1, 5))));
        pokemons.add(new Pokemon("139", "Omastar", Type.ROCA, (short)70, (short)60, (short)115, (short)125, (short)70, (short)55, new ArrayList<>(rockMoves.subList(0, 4))));
        pokemons.add(new Pokemon("141", "Kabutops", Type.ROCA, (short)60, (short)115, (short)65, (short)105, (short)70, (short)80, new ArrayList<>(rockMoves.subList(1, 5))));
        pokemons.add(new Pokemon("142", "Aerodactyl", Type.ROCA, (short)80, (short)105, (short)60, (short)65, (short)75, (short)130, new ArrayList<>(rockMoves.subList(0, 4))));
    
        // Pokémon tipo Fantasma
        pokemons.add(new Pokemon("94", "Gengar", Type.FANTASMA, (short)60, (short)65, (short)130, (short)60, (short)75, (short)110, new ArrayList<>(ghostMoves.subList(0, 4))));
        pokemons.add(new Pokemon("93", "Haunter", Type.FANTASMA, (short)45, (short)50, (short)115, (short)45, (short)55, (short)95, new ArrayList<>(ghostMoves.subList(1, 5))));
        pokemons.add(new Pokemon("92", "Gastly", Type.FANTASMA, (short)30, (short)35, (short)100, (short)30, (short)35, (short)80, new ArrayList<>(ghostMoves.subList(0, 4))));
        pokemons.add(new Pokemon("200", "Misdreavus", Type.FANTASMA, (short)60, (short)60, (short)85, (short)60, (short)85, (short)85, new ArrayList<>(ghostMoves.subList(1, 5))));
        pokemons.add(new Pokemon("105", "Marowak", Type.FANTASMA, (short)60, (short)80, (short)50, (short)110, (short)80, (short)45, new ArrayList<>(ghostMoves.subList(0, 4))));
    
        // Pokémon tipo Lucha
        pokemons.add(new Pokemon("68", "Machamp", Type.LUCHA, (short)90, (short)130, (short)65, (short)80, (short)85, (short)55, new ArrayList<>(fightingMoves.subList(0, 4))));
        pokemons.add(new Pokemon("106", "Hitmonlee", Type.LUCHA, (short)50, (short)120, (short)35, (short)53, (short)110, (short)87, new ArrayList<>(fightingMoves.subList(1, 5))));
        pokemons.add(new Pokemon("107", "Hitmonchan", Type.LUCHA, (short)50, (short)105, (short)35, (short)79, (short)110, (short)76, new ArrayList<>(fightingMoves.subList(0, 4))));
        pokemons.add(new Pokemon("57", "Primeape", Type.LUCHA, (short)65, (short)105, (short)60, (short)60, (short)70, (short)95, new ArrayList<>(fightingMoves.subList(1, 5))));
        pokemons.add(new Pokemon("62", "Poliwrath", Type.LUCHA, (short)90, (short)95, (short)70, (short)95, (short)90, (short)70, new ArrayList<>(fightingMoves.subList(0, 4))));
    
        // Pokémon tipo Normal
        pokemons.add(new Pokemon("143", "Snorlax", Type.NORMAL, (short)160, (short)110, (short)65, (short)65, (short)110, (short)30, new ArrayList<>(normalMoves.subList(0, 4))));
        pokemons.add(new Pokemon("128", "Tauros", Type.NORMAL, (short)75, (short)100, (short)40, (short)95, (short)70, (short)110, new ArrayList<>(normalMoves.subList(1, 5))));
        pokemons.add(new Pokemon("115", "Kangaskhan", Type.NORMAL, (short)105, (short)95, (short)40, (short)80, (short)80, (short)90, new ArrayList<>(normalMoves.subList(0, 4))));
        pokemons.add(new Pokemon("53", "Persian", Type.NORMAL, (short)65, (short)70, (short)65, (short)60, (short)65, (short)115, new ArrayList<>(normalMoves.subList(1, 5))));
        pokemons.add(new Pokemon("113", "Chansey", Type.NORMAL, (short)250, (short)5, (short)35, (short)5, (short)105, (short)50, new ArrayList<>(normalMoves.subList(0, 4))));
    
        // Pokémon tipo Veneno
        pokemons.add(new Pokemon("110", "Weezing", Type.VENENO, (short)65, (short)90, (short)85, (short)120, (short)70, (short)60, new ArrayList<>(poisonMoves.subList(0, 4))));
        pokemons.add(new Pokemon("89", "Muk", Type.VENENO, (short)105, (short)105, (short)65, (short)75, (short)100, (short)50, new ArrayList<>(poisonMoves.subList(1, 5))));
        pokemons.add(new Pokemon("24", "Arbok", Type.VENENO, (short)60, (short)95, (short)65, (short)69, (short)79, (short)80, new ArrayList<>(poisonMoves.subList(0, 4))));
        pokemons.add(new Pokemon("71", "Victreebel", Type.VENENO, (short)80, (short)105, (short)100, (short)65, (short)70, (short)70, new ArrayList<>(poisonMoves.subList(1, 5))));
        pokemons.add(new Pokemon("34", "Nidoking", Type.VENENO, (short)81, (short)102, (short)85, (short)77, (short)75, (short)85, new ArrayList<>(poisonMoves.subList(0, 4))));
    
        // Pokémon tipo Hada
        pokemons.add(new Pokemon("36", "Clefable", Type.HADA, (short)95, (short)70, (short)95, (short)73, (short)90, (short)60, new ArrayList<>(fairyMoves.subList(0, 4))));
        pokemons.add(new Pokemon("40", "Wigglytuff", Type.HADA, (short)140, (short)70, (short)85, (short)45, (short)50, (short)45, new ArrayList<>(fairyMoves.subList(1, 5))));
        pokemons.add(new Pokemon("122", "Mr. Mime", Type.HADA, (short)40, (short)45, (short)100, (short)65, (short)120, (short)90, new ArrayList<>(fairyMoves.subList(0, 4))));
        pokemons.add(new Pokemon("39", "Jigglypuff", Type.HADA, (short)115, (short)45, (short)45, (short)20, (short)25, (short)20, new ArrayList<>(fairyMoves.subList(1, 5))));
        pokemons.add(new Pokemon("35", "Clefairy", Type.HADA, (short)70, (short)45, (short)60, (short)48, (short)65, (short)35, new ArrayList<>(fairyMoves.subList(0, 4))));

        // Pokémon tipo Acero
        pokemons.add(new Pokemon("82", "Magneton", Type.ACERO, (short)50, (short)60, (short)120, (short)95, (short)70, (short)70, new ArrayList<>(steelMoves.subList(0, 4))));
        pokemons.add(new Pokemon("81", "Magnemite", Type.ACERO, (short)25, (short)35, (short)95, (short)70, (short)55, (short)45, new ArrayList<>(steelMoves.subList(1, 5))));
        pokemons.add(new Pokemon("208", "Steelix", Type.ACERO, (short)75, (short)85, (short)55, (short)200, (short)65, (short)30, new ArrayList<>(steelMoves.subList(0, 4))));
        pokemons.add(new Pokemon("212", "Scizor", Type.ACERO, (short)70, (short)130, (short)55, (short)100, (short)80, (short)65, new ArrayList<>(steelMoves.subList(1, 5))));
        pokemons.add(new Pokemon("227", "Skarmory", Type.ACERO, (short)65, (short)80, (short)40, (short)140, (short)70, (short)70, new ArrayList<>(steelMoves.subList(0, 4))));

        // Pokémon tipo Tierra
        pokemons.add(new Pokemon("28", "Sandslash", Type.TIERRA, (short)75, (short)100, (short)45, (short)110, (short)55, (short)65, new ArrayList<>(groundMoves.subList(0, 4))));
        pokemons.add(new Pokemon("31", "Nidoqueen", Type.TIERRA, (short)90, (short)92, (short)75, (short)87, (short)85, (short)76, new ArrayList<>(groundMoves.subList(1, 5))));
        pokemons.add(new Pokemon("51", "Dugtrio", Type.TIERRA, (short)35, (short)100, (short)50, (short)50, (short)70, (short)120, new ArrayList<>(groundMoves.subList(0, 4))));
        pokemons.add(new Pokemon("76", "Golem", Type.TIERRA, (short)80, (short)120, (short)55, (short)130, (short)65, (short)45, new ArrayList<>(groundMoves.subList(1, 5))));
        pokemons.add(new Pokemon("111", "Rhyhorn", Type.TIERRA, (short)80, (short)85, (short)30, (short)95, (short)30, (short)25, new ArrayList<>(groundMoves.subList(0, 4))));
    }

    public static void initializeData() {
        initializeMoves();
        initializePokemons();
    }

    public static ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public static ArrayList<Movement> getMoves(Type type) {
        initializeMoves();
        return switch (type) {
            case FUEGO -> fireMoves;
            case AGUA -> waterMoves;
            case PLANTA -> grassMoves;
            case TIERRA -> groundMoves;
            case ELECTRICO -> electricMoves;
            case VOLADOR -> flyingMoves;
            case PSIQUICO -> psychicMoves;
            case HIELO -> iceMoves;
            case BICHO -> bugMoves;
            case ROCA -> rockMoves;
            case FANTASMA -> ghostMoves;
            case DRAGON -> dragonMoves;
            case LUCHA -> fightingMoves;
            case HADA -> fairyMoves;
            case SINIESTRO -> darkMoves;
            case NORMAL -> normalMoves;
            case ACERO -> steelMoves;
            case VENENO -> poisonMoves;
            default -> throw new IllegalArgumentException("Tipo de Pokemon Desconocido: " + type);
        };
    }
}