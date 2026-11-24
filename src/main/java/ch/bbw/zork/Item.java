package ch.bbw.zork;

public class Item {
    private ItemType type;
    private String name;
    private final int weight;

    public Item(ItemType type, String name) {
        this.type = type;
        this.name = name;
        this.weight = defaultWeight(type);
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    private static int defaultWeight(ItemType type) {
        switch (type) {
            case FLASHLIGHT:
                return 3;
            case BATTERY:
            case CRUCIFIX:
                return 2;
            default:
                return 1;
        }
    }
}
