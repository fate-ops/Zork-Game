package ch.bbw.zork;

public class JeffShop {

    public static final int ROOM_NUMBER = 25;

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

    public void showStock() {
        System.out.println("\nJeff: Welcome! What do you need?");
        System.out.println("Crucifix   - 250 coins - buy crucifix");
        System.out.println("Lockpick   -  50 coins - buy lockpick");
        System.out.println("Flashlight - 100 coins - buy flashlight");
        System.out.println("Bandage    -  75 coins - buy bandage");
        System.out.println("Use 'continue' to leave or 'return' to go back.");
    }

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