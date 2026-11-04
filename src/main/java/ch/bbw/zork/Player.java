package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final int MAX_HP = 100;
    public static final int BANDAGE_HEAL = 15;
    public static final int BATTERY_RECHARGE = 30;

    private int hp;
    private int coins;
    private List<Item> inventory;

    private boolean flashlightOn = false;
    private int flashlightBattery = 100;
    private long flashlightActiveNanos = 0;
    private long flashlightLastUpdateNanos = 0;

    public Player() {
        this.hp = MAX_HP;
        this.coins = 0;
        this.inventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    public boolean removeItem(ItemType type) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getType() == type) {
                if (type == ItemType.FLASHLIGHT) {
                    turnFlashlightOff();
                }

                inventory.remove(i);
                return true;
            }
        }

        return false;
    }

    public boolean hasItem(ItemType type) {
        for (Item item : inventory) {
            if (item.getType() == type) {
                return true;
            }
        }

        return false;
    }

    private void updateFlashlightBattery() {
        if (!flashlightOn) {
            return;
        }

        long now = System.nanoTime();

        flashlightActiveNanos += now - flashlightLastUpdateNanos;
        flashlightLastUpdateNanos = now;

        long drainInterval = 30_000_000_000L; // 30 Sekunden
        long intervals = flashlightActiveNanos / drainInterval;

        if (intervals > 0) {
            flashlightBattery = (int) Math.max(
                    0L,
                    flashlightBattery - intervals * 10L
            );

            flashlightActiveNanos %= drainInterval;
        }

        if (flashlightBattery == 0) {
            flashlightOn = false;
            flashlightActiveNanos = 0;
        }
    }

    public boolean isFlashlightOn() {
        updateFlashlightBattery();
        return flashlightOn && hasItem(ItemType.FLASHLIGHT);
    }

    public boolean turnFlashlightOn() {
        updateFlashlightBattery();

        if (!hasItem(ItemType.FLASHLIGHT) || flashlightBattery == 0) {
            return false;
        }

        if (!flashlightOn) {
            flashlightLastUpdateNanos = System.nanoTime();
            flashlightOn = true;
        }

        return true;
    }

    public void turnFlashlightOff() {
        updateFlashlightBattery();
        flashlightOn = false;
    }

    public int getFlashlightBattery() {
        updateFlashlightBattery();
        return flashlightBattery;
    }

    public String inventoryDescription() {
        if (inventory.isEmpty()) {
            return "Your inventory is empty.";
        }

        StringBuilder description = new StringBuilder("You are carrying:\n");

        for (Item item : inventory) {
            description
                    .append("- ")
                    .append(item.getName())
                    .append(" (")
                    .append(item.getType())
                    .append(")");

            if (item.getType() == ItemType.FLASHLIGHT) {
                description
                        .append(" - Battery: ")
                        .append(getFlashlightBattery())
                        .append("%, ")
                        .append(isFlashlightOn() ? "ON" : "OFF");
            }

            description.append("\n");
        }

        return description.toString();
    }

    // Verbraucht nur dann eine Bandage, wenn tatsaechlich geheilt wird.
    public int useBandage() {
        if (!isAlive() || hp >= MAX_HP || !removeItem(ItemType.BANDAID)) {
            return 0;
        }

        int healed = Math.min(BANDAGE_HEAL, MAX_HP - hp);
        hp += healed;
        return healed;
    }

    public int useBattery() {
        updateFlashlightBattery();

        if (!hasItem(ItemType.FLASHLIGHT)
                || flashlightBattery >= 100
                || !removeItem(ItemType.BATTERY)) {
            return 0;
        }

        int charged = Math.min(BATTERY_RECHARGE, 100 - flashlightBattery);
        flashlightBattery += charged;
        return charged;
    }

    public int getHp() {
        return hp;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += Math.max(0, amount);
    }

    public void takeDamage(int amount) {
        this.hp -= Math.max(0, amount);

        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}