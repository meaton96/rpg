package resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resource {

    public static final int DEFAULT_MAX_MANA = 100;
    public static final int DEFAULT_MAX_ENERGY = 100;
    public static final int DEFAULT_MAX_RAGE = 100;

    private static final int MANA_REGEN = 10;
    private static final int ENERGY_REGEN = 50;



    public enum Type {
        MANA,
        ENERGY,
        RAGE
    }
    int maxAmount, currentAmount;
    Type type;

    public Resource(Type type) {
        switch (type) {
            case MANA :
                maxAmount = DEFAULT_MAX_MANA;
                currentAmount = maxAmount;
                break;
            case ENERGY:
                maxAmount = DEFAULT_MAX_ENERGY;
                currentAmount = maxAmount;
                break;
            case RAGE:
                maxAmount = DEFAULT_MAX_RAGE;
                currentAmount = 0;
            default:
                break;
        }
    }

    private void generateResource(int amount) {
        currentAmount += amount;
        if (currentAmount > maxAmount)
            currentAmount = maxAmount;
    }
    private boolean spendResource(int amount) {
        if (amount > currentAmount)
            return false;
        currentAmount -= amount;
        return true;
    }
    private void regenBetweenTurns() {
        switch (type) {
            case MANA: currentAmount += MANA_REGEN;
            break;
            case ENERGY: currentAmount += ENERGY_REGEN;
            break;
            default:
            break;
        }
        if (currentAmount > maxAmount)
            currentAmount = maxAmount;
    }
}
