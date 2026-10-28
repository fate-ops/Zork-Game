package ch.bbw.zork;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int hp;
    private int coins;
    private List<Item> inventory;

    private boolean flashlightOn = false;
    private int flashlightBattery = 100;
    private long flashlightActiveNanos = 0;
    private long flashlightLastUpdateNanos = 0;

    public Player() {
        this.hp = 100;
        this.coins = 0;
        this.inventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        inventory.add(item);
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

    public int getHp() {
        return hp;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public void takeDamage(int amount) {
        this.hp -= amount;

        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}