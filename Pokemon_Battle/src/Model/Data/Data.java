package Model.Data;

import Model.Movement;
import Model.Pokemon;
import Model.Type;
import Model.Exceptions.pokemonBattle.unknownPokemonTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Estructuras optimizadas para Pokémon
    private static final Map<String, Pokemon> pokemonById = new HashMap<>();    // Búsqueda por ID
    private static final Map<String, Pokemon> pokemonByName = new HashMap<>();  // Búsqueda por nombre
    private static final Map<Type, List<Pokemon>> pokemonByType = new HashMap<>(); // Agrupación por tipo
    private static final ArrayList<Pokemon> allPokemons = new ArrayList<>();    // Lista completa

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

        // Limpiar estructuras existentes
        pokemonById.clear();
        pokemonByName.clear();
        pokemonByType.clear();
        allPokemons.clear();

        // Inicializar listas por tipo
        for (Type type : Type.values()) {
            pokemonByType.put(type, new ArrayList<>());
        }

        //Pokemon tipo Fuego

        Pokemon charizard = new Pokemon("6", "Charizard", Type.FUEGO, 
            (short)78, (short)84, (short)109, (short)78, 
            (short)85, (short)100, new ArrayList<>(fireMoves.subList(0, 4)));
        
        addPokemonToMaps(charizard);

        Pokemon arcanine = new Pokemon("59", "Arcanine", Type.FUEGO, 
            (short)90, (short)110, (short)100, (short)80, 
            (short)80, (short)95, new ArrayList<>(fireMoves.subList(1, 5)));
        
        addPokemonToMaps(arcanine);

        Pokemon magmar = new Pokemon("126", "Magmar", Type.FUEGO, 
            (short)65, (short)95, (short)100, (short)57, 
            (short)85, (short)93, new ArrayList<>(fireMoves.subList(0, 4)));

        addPokemonToMaps(magmar);

        Pokemon flareon = new Pokemon("136", "Flareon", Type.FUEGO, 
            (short)65, (short)130, (short)95, (short)60, 
            (short)110, (short)65, new ArrayList<>(fireMoves.subList(1, 5)));

        addPokemonToMaps(flareon);

        Pokemon moltres = new Pokemon("146", "Moltres", Type.FUEGO, 
            (short)90, (short)100, (short)125, (short)90, 
            (short)85, (short)90, new ArrayList<>(fireMoves.subList(0, 4)));

        addPokemonToMaps(moltres);

        //Pokemon tipo Agua

        Pokemon blastoise = new Pokemon("9", "Blastoise", Type.AGUA, 
            (short)79, (short)83, (short)85, (short)100, 
            (short)105, (short)78, new ArrayList<>(waterMoves.subList(0, 4)));

        addPokemonToMaps(blastoise);

        Pokemon gyarados = new Pokemon("130", "Gyarados", Type.AGUA, 
            (short)95, (short)125, (short)60, (short)79, 
            (short)100, (short)81, new ArrayList<>(waterMoves.subList(1, 5)));

        addPokemonToMaps(gyarados);

        Pokemon vaporeon = new Pokemon("134", "Vaporeon", Type.AGUA, 
            (short)130, (short)65, (short)110, (short)60, 
            (short)95, (short)65, new ArrayList<>(waterMoves.subList(0, 4)));

        addPokemonToMaps(vaporeon);

        Pokemon starmie = new Pokemon("121", "Starmie", Type.AGUA, 
            (short)60, (short)75, (short)100, (short)85, 
            (short)85, (short)115, new ArrayList<>(waterMoves.subList(1, 5)));

        addPokemonToMaps(starmie);

        Pokemon kingler = new Pokemon("99", "Kingler", Type.AGUA, 
            (short)55, (short)130, (short)50, (short)115, 
            (short)50, (short)75, new ArrayList<>(waterMoves.subList(0, 4)));

        addPokemonToMaps(kingler);

        // Pokémon tipo Planta
    
        Pokemon venusaur = new Pokemon("3", "Venusaur", Type.PLANTA, 
            (short)80, (short)82, (short)100, (short)83, 
            (short)100, (short)80, new ArrayList<>(grassMoves.subList(0, 4)));

        addPokemonToMaps(venusaur);

        Pokemon victreebel = new Pokemon("71", "Victreebel", Type.PLANTA, 
            (short)80, (short)105, (short)100, (short)65, 
            (short)70, (short)70, new ArrayList<>(grassMoves.subList(1, 5)));

        addPokemonToMaps(victreebel);

        Pokemon vileplume = new Pokemon("45", "Vileplume", Type.PLANTA, 
            (short)75, (short)80, (short)100, (short)85, 
            (short)90, (short)50, new ArrayList<>(grassMoves.subList(0, 4)));

        addPokemonToMaps(vileplume);

        Pokemon exeggutor = new Pokemon("103", "Exeggutor", Type.PLANTA, 
            (short)95, (short)95, (short)125, (short)85, 
            (short)65, (short)55, new ArrayList<>(grassMoves.subList(1, 5)));

        addPokemonToMaps(exeggutor);

        Pokemon tangela = new Pokemon("114", "Tangela", Type.PLANTA, 
            (short)65, (short)55, (short)100, (short)115, 
            (short)40, (short)60, new ArrayList<>(grassMoves.subList(0, 4)));   

        addPokemonToMaps(tangela);

        // Pokémon tipo Eléctrico

        Pokemon raichu = new Pokemon("26", "Raichu", Type.ELECTRICO, 
            (short)60, (short)90, (short)90, (short)55, 
            (short)80, (short)110, new ArrayList<>(electricMoves.subList(0, 4)));

        addPokemonToMaps(raichu);

        Pokemon jolteon = new Pokemon("135", "Jolteon", Type.ELECTRICO, 
            (short)65, (short)65, (short)110, (short)60, 
            (short)95, (short)130, new ArrayList<>(electricMoves.subList(1, 5)));

        addPokemonToMaps(jolteon);

        Pokemon electabuzz = new Pokemon("125", "Electabuzz", Type.ELECTRICO, 
            (short)65, (short)83, (short)95, (short)57, 
            (short)85, (short)105, new ArrayList<>(electricMoves.subList(0, 4)));

        addPokemonToMaps(electabuzz);

        Pokemon zapdos = new Pokemon("145", "Zapdos", Type.ELECTRICO, 
            (short)90, (short)90, (short)125, (short)85, 
            (short)90, (short)100, new ArrayList<>(electricMoves.subList(1, 5)));
        
        addPokemonToMaps(zapdos);

        Pokemon ampharos = new Pokemon("181", "Ampharos", Type.ELECTRICO, 
            (short)90, (short)75, (short)115, (short)85, 
            (short)90, (short)55, new ArrayList<>(electricMoves.subList(0, 4)));

        addPokemonToMaps(ampharos);

        // Pokémon tipo Volador

        Pokemon pidgeot = new Pokemon("18", "Pidgeot", Type.VOLADOR, 
            (short)83, (short)80, (short)70, (short)75, 
            (short)70, (short)101, new ArrayList<>(flyingMoves.subList(0, 4)));

        addPokemonToMaps(pidgeot);

        Pokemon fearow = new Pokemon("22", "Fearow", Type.VOLADOR, 
            (short)65, (short)90, (short)61, (short)65, 
            (short)61, (short)100, new ArrayList<>(flyingMoves.subList(1, 5)));

        addPokemonToMaps(fearow);

        Pokemon aerodactyl = new Pokemon("142", "Aerodactyl", Type.VOLADOR, 
            (short)80, (short)105, (short)60, (short)65, 
            (short)75, (short)130, new ArrayList<>(flyingMoves.subList(0, 4)));

        addPokemonToMaps(aerodactyl);

        Pokemon farfetchd = new Pokemon("83", "Farfetch'd", Type.VOLADOR, 
            (short)52, (short)90, (short)58, (short)55, 
            (short)62, (short)60, new ArrayList<>(flyingMoves.subList(1, 5)));

        addPokemonToMaps(farfetchd);

        Pokemon dodrio = new Pokemon("85", "Dodrio", Type.VOLADOR, 
            (short)60, (short)110, (short)60, (short)70, 
            (short)60, (short)110, new ArrayList<>(flyingMoves.subList(0, 4)));

        addPokemonToMaps(dodrio);

        // Pokémon tipo Psíquico

        Pokemon alakazam = new Pokemon("65", "Alakazam", Type.PSIQUICO, 
            (short)55, (short)50, (short)135, (short)45, 
            (short)95, (short)120, new ArrayList<>(psychicMoves.subList(0, 4)));

        addPokemonToMaps(alakazam);

        Pokemon slowbro = new Pokemon("80", "Slowbro", Type.PSIQUICO, 
            (short)95, (short)75, (short)100, (short)110, 
            (short)80, (short)30, new ArrayList<>(psychicMoves.subList(1, 5)));

        addPokemonToMaps(slowbro);

        Pokemon hypno = new Pokemon("97", "Hypno", Type.PSIQUICO, 
            (short)85, (short)73, (short)73, (short)70, 
            (short)115, (short)67, new ArrayList<>(psychicMoves.subList(0, 4)));

        addPokemonToMaps(hypno);

        Pokemon mewtwo = new Pokemon("150", "Mewtwo", Type.PSIQUICO, 
            (short)106, (short)110, (short)154, (short)90, 
            (short)90, (short)130, new ArrayList<>(psychicMoves.subList(1, 5)));

        addPokemonToMaps(mewtwo);

        Pokemon mew = new Pokemon("151", "Mew", Type.PSIQUICO, 
            (short)100, (short)100, (short)100, (short)100, 
            (short)100, (short)100, new ArrayList<>(psychicMoves.subList(0, 4)));

        addPokemonToMaps(mew);

        // Pokémon tipo Hielo

        Pokemon articuno = new Pokemon("144", "Articuno", Type.HIELO, 
            (short)90, (short)85, (short)95, (short)100, 
            (short)125, (short)85, new ArrayList<>(iceMoves.subList(0, 4)));

        addPokemonToMaps(articuno);

        Pokemon jynx = new Pokemon("124", "Jynx", Type.HIELO, 
            (short)65, (short)50, (short)115, (short)35, 
            (short)95, (short)95, new ArrayList<>(iceMoves.subList(1, 5)));

        addPokemonToMaps(jynx);

        Pokemon dewgong = new Pokemon("87", "Dewgong", Type.HIELO, 
            (short)90, (short)70, (short)70, (short)80, 
            (short)95, (short)70, new ArrayList<>(iceMoves.subList(0, 4)));

        addPokemonToMaps(dewgong);

        Pokemon cloyster = new Pokemon("91", "Cloyster", Type.HIELO, 
            (short)50, (short)95, (short)85, (short)180, 
            (short)45, (short)70, new ArrayList<>(iceMoves.subList(1, 5)));
        
        addPokemonToMaps(cloyster);

        Pokemon lapras = new Pokemon("131", "Lapras", Type.HIELO, 
            (short)130, (short)85, (short)85, (short)80, 
            (short)95, (short)60, new ArrayList<>(iceMoves.subList(0, 4)));

        addPokemonToMaps(lapras);

        // Pokémon tipo Bicho

        Pokemon butterfree = new Pokemon("12", "Butterfree", Type.BICHO, 
            (short)60, (short)45, (short)90, (short)50, 
            (short)80, (short)70, new ArrayList<>(bugMoves.subList(0, 4)));

        addPokemonToMaps(butterfree);

        Pokemon beedrill = new Pokemon("15", "Beedrill", Type.BICHO, 
            (short)65, (short)90, (short)45, (short)40, 
            (short)80, (short)75, new ArrayList<>(bugMoves.subList(1, 5)));

        addPokemonToMaps(beedrill);

        Pokemon pinsir = new Pokemon("127", "Pinsir", Type.BICHO, 
            (short)65, (short)125, (short)55, (short)100, 
            (short)70, (short)85, new ArrayList<>(bugMoves.subList(0, 4)));

        addPokemonToMaps(pinsir);

        Pokemon scyther = new Pokemon("123", "Scyther", Type.BICHO, 
            (short)70, (short)110, (short)55, (short)80, 
            (short)80, (short)105, new ArrayList<>(bugMoves.subList(1, 5)));
        
        addPokemonToMaps(scyther);

        Pokemon parasect = new Pokemon("47", "Parasect", Type.BICHO, 
            (short)60, (short)95, (short)60, (short)80, 
            (short)80, (short)30, new ArrayList<>(bugMoves.subList(0, 4)));

        addPokemonToMaps(parasect);

        // Pokémon tipo Roca

        Pokemon golem = new Pokemon("76", "Golem", Type.ROCA, 
            (short)80, (short)120, (short)55, (short)130, 
            (short)65, (short)45, new ArrayList<>(rockMoves.subList(0, 4)));

        addPokemonToMaps(golem);

        Pokemon rhydon = new Pokemon("112", "Rhydon", Type.ROCA, 
            (short)105, (short)130, (short)45, (short)120, 
            (short)45, (short)40, new ArrayList<>(rockMoves.subList(1, 5)));

        addPokemonToMaps(rhydon);

        Pokemon omastar = new Pokemon("139", "Omastar", Type.ROCA, 
            (short)70, (short)60, (short)115, (short)125, 
            (short)70, (short)55, new ArrayList<>(rockMoves.subList(0, 4)));

        addPokemonToMaps(omastar);

        Pokemon kabutops = new Pokemon("141", "Kabutops", Type.ROCA, 
            (short)60, (short)115, (short)65, (short)105, 
            (short)70, (short)80, new ArrayList<>(rockMoves.subList(1, 5)));

        addPokemonToMaps(kabutops);

        Pokemon onix = new Pokemon("95", "Onix", Type.ROCA, 
            (short)35, (short)45, (short)30, (short)160, 
            (short)45, (short)70, new ArrayList<>(rockMoves.subList(0, 4)));

        addPokemonToMaps(onix);

        // Pokémon tipo Fantasma

        Pokemon gengar = new Pokemon("94", "Gengar", Type.FANTASMA, 
            (short)60, (short)65, (short)130, (short)60, 
            (short)75, (short)110, new ArrayList<>(ghostMoves.subList(0, 4)));

        addPokemonToMaps(gengar);

        Pokemon haunter = new Pokemon("93", "Haunter", Type.FANTASMA, 
            (short)45, (short)50, (short)115, (short)45, 
            (short)55, (short)95, new ArrayList<>(ghostMoves.subList(1, 5)));

        addPokemonToMaps(haunter);

        Pokemon gastly = new Pokemon("92", "Gastly", Type.FANTASMA, 
            (short)30, (short)35, (short)100, (short)30, 
            (short)35, (short)80, new ArrayList<>(ghostMoves.subList(0, 4)));

        addPokemonToMaps(gastly);

        Pokemon misdreavus = new Pokemon("200", "Misdreavus", Type.FANTASMA, 
            (short)60, (short)60, (short)60, (short)85, 
            (short)85, (short)85, new ArrayList<>(ghostMoves.subList(1, 5)));

        addPokemonToMaps(misdreavus);

        Pokemon marowak = new Pokemon("105", "Marowak", Type.FANTASMA, 
            (short)60, (short)80, (short)110, (short)50, 
            (short)80, (short)45, new ArrayList<>(ghostMoves.subList(0, 4)));

        addPokemonToMaps(marowak);

        // Pokémon tipo Lucha

        Pokemon machamp = new Pokemon("68", "Machamp", Type.LUCHA, 
            (short)90, (short)130, (short)80, (short)55, 
            (short)65, (short)55, new ArrayList<>(fightingMoves.subList(0, 4)));

        addPokemonToMaps(machamp);

        Pokemon hitmonlee = new Pokemon("106", "Hitmonlee", Type.LUCHA, 
            (short)50, (short)120, (short)53, (short)35, 
            (short)110, (short)87, new ArrayList<>(fightingMoves.subList(1, 5)));

        addPokemonToMaps(hitmonlee);

        Pokemon hitmonchan = new Pokemon("107", "Hitmonchan", Type.LUCHA, 
            (short)50, (short)105, (short)79, (short)35, 
            (short)110, (short)76, new ArrayList<>(fightingMoves.subList(0, 4)));

        addPokemonToMaps(hitmonchan);

        Pokemon primeape = new Pokemon("57", "Primeape", Type.LUCHA, 
            (short)65, (short)105, (short)60, (short)70, 
            (short)70, (short)95, new ArrayList<>(fightingMoves.subList(1, 5)));

        addPokemonToMaps(primeape);

        Pokemon poliwrath = new Pokemon("62", "Poliwrath", Type.LUCHA, 
            (short)90, (short)95, (short)95, (short)70, 
            (short)70, (short)70, new ArrayList<>(fightingMoves.subList(0, 4)));

        addPokemonToMaps(poliwrath);

        // Pokémon tipo Hada

        Pokemon clefable = new Pokemon("36", "Clefable", Type.HADA, 
            (short)95, (short)70, (short)73, (short)60, 
            (short)85, (short)60, new ArrayList<>(fairyMoves.subList(0, 4)));

        addPokemonToMaps(clefable);

        Pokemon jigglypuff = new Pokemon("39", "Jigglypuff", Type.HADA, 
            (short)115, (short)45, (short)20, (short)25, 
            (short)45, (short)20, new ArrayList<>(fairyMoves.subList(1, 5)));

        addPokemonToMaps(jigglypuff);

        Pokemon clefairy = new Pokemon("35", "Clefairy", Type.HADA, 
            (short)70, (short)45, (short)48, (short)35, 
            (short)60, (short)35, new ArrayList<>(fairyMoves.subList(0, 4)));

        addPokemonToMaps(clefairy);

        Pokemon mr_mime = new Pokemon("122", "Mr. Mime", Type.HADA, 
            (short)40, (short)45, (short)65, (short)90, 
            (short)100, (short)70, new ArrayList<>(fairyMoves.subList(1, 5)));

        addPokemonToMaps(mr_mime);

        // Pokémon tipo Siniestro

        Pokemon umbreon = new Pokemon("197", "Umbreon", Type.SINIESTRO, 
            (short)95, (short)65, (short)60, (short)110, 
            (short)130, (short)65, new ArrayList<>(darkMoves.subList(0, 4)));

        addPokemonToMaps(umbreon);

        Pokemon houndoom = new Pokemon("229", "Houndoom", Type.SINIESTRO, 
            (short)75, (short)90, (short)50, (short)110, 
            (short)80, (short)95, new ArrayList<>(darkMoves.subList(1, 5)));

        addPokemonToMaps(houndoom);

        Pokemon murkrow = new Pokemon("198", "Murkrow", Type.SINIESTRO, 
            (short)60, (short)85, (short)42, (short)85, 
            (short)42, (short)91, new ArrayList<>(darkMoves.subList(0, 4)));

        addPokemonToMaps(murkrow);

        Pokemon sneasel = new Pokemon("215", "Sneasel", Type.SINIESTRO, 
            (short)55, (short)95, (short)55, (short)35, 
            (short)75, (short)115, new ArrayList<>(darkMoves.subList(1, 5)));

        addPokemonToMaps(sneasel);

        // Pokémon tipo Normal

        Pokemon snorlax = new Pokemon("143", "Snorlax", Type.NORMAL, 
            (short)160, (short)110, (short)65, (short)30, 
            (short)110, (short)30, new ArrayList<>(normalMoves.subList(0, 4)));

        addPokemonToMaps(snorlax);

        Pokemon tauros = new Pokemon("128", "Tauros", Type.NORMAL, 
            (short)75, (short)100, (short)95, (short)70, 
            (short)70, (short)110, new ArrayList<>(normalMoves.subList(1, 5)));
        
        addPokemonToMaps(tauros);

        Pokemon kangaskhan = new Pokemon("115", "Kangaskhan", Type.NORMAL, 
            (short)105, (short)95, (short)80, (short)90, 
            (short)100, (short)90, new ArrayList<>(normalMoves.subList(0, 4)));

        addPokemonToMaps(kangaskhan);

        Pokemon persian = new Pokemon("53", "Persian", Type.NORMAL, 
            (short)65, (short)70, (short)60, (short)65, 
            (short)65, (short)115, new ArrayList<>(normalMoves.subList(1, 5)));

        addPokemonToMaps(persian);

        Pokemon chansey = new Pokemon("113", "Chansey", Type.NORMAL, 
            (short)250, (short)5, (short)5, (short)50, 
            (short)105, (short)50, new ArrayList<>(normalMoves.subList(0, 4)));

        addPokemonToMaps(chansey);

        // Pokémon tipo Veneno

        Pokemon weezing = new Pokemon("110", "Weezing", Type.VENENO, 
            (short)65, (short)90, (short)85, (short)120, 
            (short)70, (short)60, new ArrayList<>(poisonMoves.subList(0, 4)));

        addPokemonToMaps(weezing);

        Pokemon muk = new Pokemon("89", "Muk", Type.VENENO, 
            (short)105, (short)105, (short)65, (short)75, 
            (short)100, (short)50, new ArrayList<>(poisonMoves.subList(1, 5)));

        addPokemonToMaps(muk);

        Pokemon arbok = new Pokemon("24", "Arbok", Type.VENENO, 
            (short)60, (short)95, (short)65, (short)69, 
            (short)79, (short)80, new ArrayList<>(poisonMoves.subList(0, 4)));

        addPokemonToMaps(arbok);

        // Reemplazado: Victreebel tipo Veneno por Crobat tipo Veneno
        Pokemon crobat = new Pokemon("169", "Crobat", Type.VENENO, 
            (short)85, (short)90, (short)70, (short)80, 
            (short)80, (short)130, new ArrayList<>(poisonMoves.subList(1, 5)));

        addPokemonToMaps(crobat);

        Pokemon nidoking = new Pokemon("34", "Nidoking", Type.VENENO, 
            (short)81, (short)102, (short)85, (short)77, 
            (short)75, (short)85, new ArrayList<>(poisonMoves.subList(0, 4)));

        addPokemonToMaps(nidoking);

        // Pokémon tipo Acero

        Pokemon magneton = new Pokemon("82", "Magneton", Type.ACERO, 
            (short)50, (short)60, (short)120, (short)95, 
            (short)70, (short)70, new ArrayList<>(steelMoves.subList(0, 4)));

        addPokemonToMaps(magneton);

        Pokemon magnemite = new Pokemon("81", "Magnemite", Type.ACERO, 
            (short)25, (short)35, (short)95, (short)70, 
            (short)55, (short)45, new ArrayList<>(steelMoves.subList(1, 5)));

        addPokemonToMaps(magnemite);

        Pokemon steelix = new Pokemon("208", "Steelix", Type.ACERO, 
            (short)75, (short)85, (short)55, (short)200, 
            (short)65, (short)30, new ArrayList<>(steelMoves.subList(0, 4)));

        addPokemonToMaps(steelix);

        Pokemon scizor = new Pokemon("212", "Scizor", Type.ACERO, 
            (short)70, (short)130, (short)55, (short)100, 
            (short)80, (short)65, new ArrayList<>(steelMoves.subList(1, 5)));

        addPokemonToMaps(scizor);

        Pokemon skarmory = new Pokemon("227", "Skarmory", Type.ACERO, 
            (short)65, (short)80, (short)40, (short)140, 
            (short)70, (short)70, new ArrayList<>(steelMoves.subList(0, 4)));

        addPokemonToMaps(skarmory);

        // Pokémon tipo Tierra

        Pokemon sandslash = new Pokemon("28", "Sandslash", Type.TIERRA, 
            (short)75, (short)100, (short)45, (short)110, 
            (short)55, (short)65, new ArrayList<>(groundMoves.subList(0, 4)));

        addPokemonToMaps(sandslash);

        Pokemon nidoqueen = new Pokemon("31", "Nidoqueen", Type.TIERRA, 
            (short)90, (short)92, (short)75, (short)87, 
            (short)85, (short)76, new ArrayList<>(groundMoves.subList(1, 5)));

        addPokemonToMaps(nidoqueen);

        Pokemon dugtrio = new Pokemon("51", "Dugtrio", Type.TIERRA, 
            (short)35, (short)100, (short)50, (short)50, 
            (short)70, (short)120, new ArrayList<>(groundMoves.subList(0, 4)));

        addPokemonToMaps(dugtrio);
    



        
        Pokemon donphan = new Pokemon("232", "Donphan", Type.TIERRA, 
            (short)90, (short)120, (short)60, (short)120, 
            (short)60, (short)50, new ArrayList<>(groundMoves.subList(1, 5)));

        addPokemonToMaps(donphan);

        Pokemon rhyhorn = new Pokemon("111", "Rhyhorn", Type.TIERRA, 
            (short)80, (short)85, (short)95, (short)30, 
            (short)30, (short)25, new ArrayList<>(groundMoves.subList(0, 4)));  
    
        addPokemonToMaps(rhyhorn);
    }

    public static void initializeData() {
        initializeMoves();
        initializePokemons();
    }

    private static void addPokemonToMaps(Pokemon pokemon) {
        // Agregar a todas las estructuras de datos
        pokemonById.put(pokemon.getPokedexId(), pokemon);
        pokemonByName.put(pokemon.getName().toLowerCase(), pokemon);
        pokemonByType.get(pokemon.getType()).add(pokemon);
        allPokemons.add(pokemon);
    }

    // Métodos de búsqueda optimizados
    
    /**
     * Obtiene un Pokémon por su ID de Pokédex
     * @param id El ID del Pokémon (ej. "6" para Charizard)
     * @return El Pokémon encontrado o null si no existe
     */
    public static Pokemon getPokemonById(String id) {
        return pokemonById.get(id);
    }
    
    /**
     * Obtiene un Pokémon por su nombre (insensible a mayúsculas)
     * @param name Nombre del Pokémon (ej. "charizard")
     * @return El Pokémon encontrado o null si no existe
     */
    public static Pokemon getPokemonByName(String name) {
        return pokemonByName.get(name.toLowerCase());
    }
    
    /**
     * Obtiene todos los Pokémon de un tipo específico
     * @param type El tipo de Pokémon a buscar
     * @return Lista de Pokémon del tipo especificado
     */
    public static List<Pokemon> getPokemonsByType(Type type) {
        return pokemonByType.getOrDefault(type, new ArrayList<>());
    }
    
    /**
     * Obtiene todos los Pokémon en forma de lista
     * @return Lista completa de todos los Pokémon
     */
    public static ArrayList<Pokemon> getPokemons() {
        return new ArrayList<>(allPokemons);
    }

    public static ArrayList<Movement> getMoves(Type type) throws unknownPokemonTypeException {
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
            default -> throw new unknownPokemonTypeException("Tipo de Pokemon Desconocido: " + type); //Exception 
        };
    }
}