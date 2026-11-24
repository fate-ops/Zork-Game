package ch.bbw.zork;

/**
 * Ein Gegenstand im Spiel, der in einem {@link Room} liegen oder vom
 * {@link Player} getragen werden kann. Jedes Item hat einen Typ, einen
 * Namen und ein Gewicht, das sich aus dem Typ ergibt.
 */
public class Item {
    private ItemType type;
    private String name;
    private final int weight;

    /**
     * Erstellt einen Gegenstand. Das Gewicht wird automatisch aus dem Typ
     * bestimmt.
     *
     * @param type der Typ des Gegenstands
     * @param name der Anzeigename des Gegenstands
     */
    public Item(ItemType type, String name) {
        this.type = type;
        this.name = name;
        this.weight = defaultWeight(type);
    }

    /**
     * Liefert den Typ des Gegenstands.
     *
     * @return der Typ, zum Beispiel {@link ItemType#KEY}
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Liefert den Anzeigenamen des Gegenstands.
     *
     * @return der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Liefert das Gewicht des Gegenstands. Es zählt für das Maximalgewicht
     * des Spielers.
     *
     * @return das Gewicht
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Bestimmt das Gewicht anhand des Typs: Flashlight 3, Batterie und
     * Crucifix 2, alle anderen Typen 1.
     *
     * @param type der Typ des Gegenstands
     * @return das Standardgewicht für diesen Typ
     */
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