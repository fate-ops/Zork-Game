package ch.bbw.zork;

public class Player {
    private int hp;
    private int coins;

    public Player() {
        this.hp = 100;
        this.coins = 0;
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
