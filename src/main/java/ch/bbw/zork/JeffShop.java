package ch.bbw.zork;

/**
 * Jeffs Shop in Raum 25. Hier kann der Spieler mit gesammelten Coins
 * Crucifix, Lockpick, Flashlight und Bandage kaufen. Der Shop ist ein
 * sicherer Raum, in dem keine Monster erscheinen.
 */
public class JeffShop {

    /** Nummer des Raums, in dem der Shop steht. */
    public static final int ROOM_NUMBER = 25;

    /** ASCII-Layout des Shops, das beim Betreten angezeigt wird. */
    public static final String LAYOUT = String.join("\n",
            "+----------------------[ DOOR 26 ]-----------------------+",
            "|                                                       |",
            "|                 JEFF'S SHOP - ROOM 25                  |",
            "|                                                       |",
            "|   [Crucifix]  [Lockpick]  [Flashlight]  [Bandage]        |",
            "|                                                       |",
            "|       +---------------------------------------+       |",
            "|       |               COUNTER                 |       |",
            "|       +---------------------------------------+       |",
            "|                       ( o o )                         |",
            "|                     ~~~ JEFF ~~~                      |",
            "|                                                       |",
            "|       Take a breath. You are safe in here.             |",
            "|                                                       |",
            "+----------------------[ DOOR 25 ]-----------------------+"
    );

    /**
     * Gibt das Angebot des Shops mit Preisen und Kaufbefehlen aus.
     */
    public void showStock() {
        System.out.println("\nJeff: Welcome! What do you need?");
        System.out.println("Crucifix   - 250 coins - buy crucifix");
        System.out.println("Lockpick   -  50 coins - buy lockpick");
        System.out.println("Flashlight - 100 coins - buy flashlight");
        System.out.println("Bandage    -  75 coins - buy bandage");
        System.out.println("Use 'continue' to leave or 'return' to go back.");
    }

    /**
     * Verkauft ein Produkt an den Spieler. Der Kauf scheitert mit einer
     * Meldung, wenn das Produkt unbekannt ist, das Inventar zu schwer wäre
     * oder die Coins nicht reichen. Eine gekaufte Flashlight ersetzt die
     * bisherige und ist voll geladen.
     *
     * @param product der Produktname in Kleinbuchstaben, zum Beispiel
     *                {@code "crucifix"}
     * @param player  der Spieler, der bezahlt und den Gegenstand erhält
     */
    public void buy(String product, Player player) {
        ItemType type;
        String name;
        int price;

        if ("crucifix".equals(product)) {
            type = ItemType.CRUCIFIX;
            name = "Crucifix";
            price = 250;

        } else if ("lockpick".equals(product)) {
            type = ItemType.LOCKPICK;
            name = "Lockpick";
            price = 50;

        } else if ("flashlight".equals(product)) {
            type = ItemType.FLASHLIGHT;
            name = "Flashlight";
            price = 100;

        } else if ("bandage".equals(product)) {
            type = ItemType.BANDAID;
            name = "Bandage";
            price = 75;

        } else {
            System.out.println(
                    "Use: buy crucifix / buy lockpick / buy flashlight / buy bandage"
            );
            return;
        }

        Item item = new Item(type, name);

        boolean replacesFlashlight = type == ItemType.FLASHLIGHT
                && player.hasItem(ItemType.FLASHLIGHT);

        if (!replacesFlashlight && !player.canCarry(item)) {
            System.out.println("Jeff: Your inventory is too heavy for that.");
            return;
        }

        if (!player.spendCoins(price)) {
            System.out.println(
                    "Jeff: You need " + price + " coins. You have "
                            + player.getCoins() + "."
            );
            return;
        }

        if (type == ItemType.FLASHLIGHT) {
            player.replaceFlashlight();
            System.out.println(
                    "Jeff: Here is your new flashlight. Battery: 100%."
            );
        } else {
            player.addItem(new Item(type, name));
            System.out.println("Jeff: Here is your " + name + ".");
        }

        System.out.println(
                "Paid: " + price + " coins. Remaining: " + player.getCoins()
        );
    }
}