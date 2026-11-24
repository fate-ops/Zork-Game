package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

/**
 * Der Spieler mit Lebenspunkten, Coins, Inventar, Flashlight und
 * Crucifix-Schutz. Das Inventar ist durch ein Maximalgewicht begrenzt.
 */
public class Player {

    /** Maximale und Start-Lebenspunkte des Spielers. */
    public static final int MAX_HP = 100;
    /** Maximales Gewicht, das der Spieler tragen kann. */
    public static final int MAX_WEIGHT = 15;
    /** Lebenspunkte, die eine Bandage höchstens heilt. */
    public static final int BANDAGE_HEAL = 15;
    /** Prozentpunkte, die eine Batterie höchstens nachlädt. */
    public static final int BATTERY_RECHARGE = 30;

    private int hp;
    private int coins;
    private List<Item> inventory;

    private boolean crucifixProtection = false;

    /**
     * Erstellt einen Spieler mit vollen HP, ohne Coins und mit leerem
     * Inventar.
     */
    public Player() {
        this.hp = MAX_HP;
        this.coins = 0;
        this.inventory = new ArrayList<>();
    }

    /**
     * Legt einen Gegenstand ins Inventar. {@code null} wird ignoriert. Das
     * Maximalgewicht wird hier nicht geprüft, dafür gibt es
     * {@link #canCarry(Item)}.
     *
     * @param item der Gegenstand
     */
    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    /**
     * Berechnet das Gewicht aller getragenen Gegenstände.
     *
     * @return das Gesamtgewicht des Inventars
     */
    public int getCarriedWeight() {
        int total = 0;

        for (Item item : inventory) {
            total += item.getWeight();
        }

        return total;
    }

    /**
     * Prüft, ob ein Gegenstand noch ins Inventar passt, ohne das
     * Maximalgewicht zu überschreiten.
     *
     * @param item der Gegenstand
     * @return {@code true}, wenn der Gegenstand getragen werden kann
     */
    public boolean canCarry(Item item) {
        return item != null
                && item.getWeight() <= MAX_WEIGHT - getCarriedWeight();
    }

    /**
     * Entfernt einen Gegenstand eines Typs aus dem Inventar.
     *
     * @param type der Typ des Gegenstands
     * @return {@code true}, wenn ein Gegenstand entfernt wurde
     */
    public boolean removeItem(ItemType type) {
        return takeItem(type) != null;
    }

    /**
     * Nimmt den ersten Gegenstand eines Typs aus dem Inventar. Wird eine
     * Flashlight entfernt, schaltet sie sich aus.
     *
     * @param type der Typ des Gegenstands
     * @return der entfernte Gegenstand, oder {@code null}, wenn keiner
     *         dieses Typs vorhanden ist
     */
    public Item takeItem(ItemType type) {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);

            if (item.getType() == type) {
                if (item instanceof Flashlight) {
                    ((Flashlight) item).turnOff();
                }

                return inventory.remove(i);
            }
        }

        return null;
    }

    /**
     * Prüft, ob der Spieler einen Gegenstand eines Typs besitzt.
     *
     * @param type der Typ des Gegenstands
     * @return {@code true}, wenn mindestens ein solcher Gegenstand
     *         vorhanden ist
     */
    public boolean hasItem(ItemType type) {
        for (Item item : inventory) {
            if (item.getType() == type) {
                return true;
            }
        }

        return false;
    }


    /** @return die erste Taschenlampe im Inventar oder null */
    private Flashlight findFlashlight() {
        for (Item item : inventory) {
            if (item instanceof Flashlight) {
                return (Flashlight) item;
            }
        }
        return null;
    }

    /** @return ob die Taschenlampe gerade leuchtet */
    public boolean isFlashlightOn() {
        Flashlight flashlight = findFlashlight();
        return flashlight != null && flashlight.isOn();
    }

    /** @return ob sich die Taschenlampe einschalten liess */
    public boolean turnFlashlightOn() {
        Flashlight flashlight = findFlashlight();
        return flashlight != null && flashlight.turnOn();
    }

    /** Schaltet die Taschenlampe aus, sofern eine vorhanden ist. */
    public void turnFlashlightOff() {
        Flashlight flashlight = findFlashlight();
        if (flashlight != null) {
            flashlight.turnOff();
        }
    }

    /** @return den Ladestand in Prozent, oder 0 ohne Taschenlampe */
    public int getFlashlightBattery() {
        Flashlight flashlight = findFlashlight();
        return flashlight == null ? 0 : flashlight.getBattery();
    }

    /** Ersetzt die bisherige Taschenlampe durch eine voll geladene. */
    public void replaceFlashlight() {
        removeItem(ItemType.FLASHLIGHT);
        addItem(new Flashlight());
    }

    /**
     * Lädt die Taschenlampe mit einer Batterie nach.
     *
     * @return die nachgeladenen Prozentpunkte, oder 0 ohne Aufladung
     */
    public int useBattery() {
        Flashlight flashlight = findFlashlight();
        if (flashlight == null || flashlight.getBattery() == 100
                || !removeItem(ItemType.BATTERY)) {
            return 0;
        }

        return flashlight.recharge(BATTERY_RECHARGE);
    }

    /**
     * Heilt den Spieler mit einer Bandage um bis zu {@link #BANDAGE_HEAL}
     * HP, höchstens bis {@link #MAX_HP}.
     *
     * @return die geheilten HP, 0 wenn nichts geheilt wurde (zum Beispiel
     *         bei vollen HP, dann bleibt die Bandage erhalten)
     */
    public int useBandage() {
        // Bei vollen HP wird die Bandage nicht entfernt.
        if (!isAlive() || hp >= MAX_HP || !removeItem(ItemType.BANDAID)) {
            return 0;
        }

        int healed = Math.min(BANDAGE_HEAL, MAX_HP - hp);
        hp += healed;

        return healed;
    }

    /**
     * Gibt an, ob der Crucifix-Schutz aktiv ist.
     *
     * @return {@code true}, wenn der nächste Treffer blockiert wird
     */
    public boolean hasCrucifixProtection() {
        return crucifixProtection;
    }

    /**
     * Aktiviert den Crucifix-Schutz und verbraucht dafür ein Crucifix.
     *
     * @return {@code true}, wenn der Schutz aktiviert wurde,
     *         {@code false} ohne Crucifix oder bei schon aktivem Schutz
     */
    public boolean activateCrucifix() {
        // Aktiver Schutz kann nicht durch weitere Items gestapelt werden.
        if (!isAlive()
                || crucifixProtection
                || !removeItem(ItemType.CRUCIFIX)) {
            return false;
        }

        crucifixProtection = true;
        return true;
    }

    /**
     * Beschreibt das Inventar mit allen Gegenständen, dem Zustand der
     * Flashlight und dem Gesamtgewicht.
     *
     * @return der Text für die Anzeige mit {@code inventory}
     */
    public String inventoryDescription() {
        String weightLine = "Weight: " + getCarriedWeight() + "/" + MAX_WEIGHT;

        if (inventory.isEmpty()) {
            return "Your inventory is empty.\n" + weightLine;
        }

        StringBuilder description = new StringBuilder(
                "You are carrying:\n"
        );

        for (Item item : inventory) {
            description
                    .append("- ")
                    .append(item.getName())
                    .append(" (")
                    .append(item.getType())
                    .append(")");

            if (item instanceof Flashlight) {
                Flashlight flashlight = (Flashlight) item;
                description
                        .append(" - Battery: ")
                        .append(flashlight.getBattery())
                        .append("%, ")
                        .append(flashlight.isOn() ? "ON" : "OFF");
            }

            description.append("\n");
        }

        return description.append(weightLine).toString();
    }

    /**
     * Liefert die aktuellen Lebenspunkte.
     *
     * @return die HP von 0 bis {@link #MAX_HP}
     */
    public int getHp() {
        return hp;
    }

    /**
     * Liefert die Anzahl Coins des Spielers.
     *
     * @return die Coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Fügt Coins hinzu. Negative Beträge werden ignoriert.
     *
     * @param amount die Anzahl Coins
     */
    public void addCoins(int amount) {
        coins += Math.max(0, amount);
    }

    /**
     * Gibt Coins aus, wenn genug vorhanden sind.
     *
     * @param amount der Betrag
     * @return {@code true}, wenn bezahlt wurde, {@code false} bei
     *         negativem Betrag oder zu wenig Coins
     */
    public boolean spendCoins(int amount) {
        if (amount < 0 || coins < amount) {
            return false;
        }

        coins -= amount;
        return true;
    }

    /**
     * Fügt dem Spieler Schaden zu. Ein aktiver Crucifix-Schutz blockiert
     * genau einen Treffer, egal wie hoch er ist. Die HP sinken nie unter 0.
     *
     * @param amount der Schaden, Werte bis 0 werden ignoriert
     */
    public void takeDamage(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }

        // Blockiert genau einen Treffer, egal wie hoch der Schaden ist.
        if (crucifixProtection) {
            crucifixProtection = false;

            System.out.println(
                    "Your crucifix blocked the damage. Its protection is gone."
            );
            return;
        }

        hp -= amount;

        if (hp < 0) {
            hp = 0;
        }
    }

    /**
     * Gibt an, ob der Spieler noch lebt.
     *
     * @return {@code true}, wenn die HP über 0 liegen
     */
    public boolean isAlive() {
        return hp > 0;
    }
}