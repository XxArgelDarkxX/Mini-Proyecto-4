package Model;

public class Movement {
    public enum TypeAttack {
        FISICO, ESPECIAL
    };
    private String name;
    public Type type;
    public TypeAttack typeAttack;
    private int power;
    private int accuracy;

    public Movement(){}

    public Movement(String name, String typeAttack, String type, byte power) {
        this.name = name;
        this.typeAttack = TypeAttack.valueOf(typeAttack.toUpperCase());
        this.type = Type.valueOf(type.toUpperCase());
        this.power = power;
        this.accuracy = 100;
    }
    
    // Constructor compatible con API
    public Movement(String name, String type, int power, int accuracy) {
        this.name = name;
        this.type = Type.valueOf(type.toUpperCase());
        this.power = power;
        this.accuracy = accuracy;
        this.typeAttack = TypeAttack.FISICO; // Valor por defecto
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }
    
    public int getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public TypeAttack getTypeAttack() {
        return typeAttack;
    }

    public void setTypeAttack(TypeAttack typeAttack) {
        this.typeAttack = typeAttack;
    }

}