package Model;

public enum Type {
    FIRE, WATER, GRASS, GROUND,
    ELECTRIC, FLYING, PSYCHIC,
    ICE, BUG, ROCK, GHOST,
    DRAGON, FIGHTING, FAIRY, DARK, NORMAL,
    STEEL, POISON;

    public String getName() {
        return this.name().toLowerCase();
    }
}