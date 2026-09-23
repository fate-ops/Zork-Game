package ch.bbw.zork;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int hp;
    private int coins;
    private List<Item> inventory;

    public Player() {
        this.hp = 100;
        this.coins = 0;
        inventory = new ArrayList<>();
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean hasItem(ItemType type) {
        for (Item item : inventory) {
            if (item.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public String inventoryDescription() {
        StringBuilder description = new StringBuilder("You are carrying:\n");
        for (Item item : inventory) {
            description.append("  ").append(item.getName()).append(" (").append(item.getType()).append(")\n");
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
