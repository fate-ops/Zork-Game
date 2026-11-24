package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests fuer die Klasse Player: Leben, Schaden, Crucifix und Bandagen.
 */
public class PlayerTests {

    private Player player;

    /**
     * Erstellt vor jedem Test einen frischen Spieler.
     */
    @Before
    public void setUp() {
        player = new Player();
    }

    /**
     * Ein neuer Spieler startet mit vollen HP, ohne Coins und lebt.
     */
    @Test
    public void newPlayerHasFullHpAndNoCoins() {
        assertEquals(Player.MAX_HP, player.getHp());
        assertEquals(0, player.getCoins());
        assertTrue(player.isAlive());
    }

    /**
     * Schaden verringert die HP, tödlicher Schaden lässt sie bei 0 enden.
     */
    @Test
    public void damageReducesHpButNeverBelowZero() {
        player.takeDamage(30);
        assertEquals(70, player.getHp());

        player.takeDamage(500);
        assertEquals(0, player.getHp());
        assertFalse(player.isAlive());
    }

    /**
     * Schaden von 0 oder weniger verändert die HP nicht.
     */
    @Test
    public void nonPositiveDamageIsIgnored() {
        player.takeDamage(0);
        player.takeDamage(-20);

        assertEquals(Player.MAX_HP, player.getHp());
    }

    /**
     * Ein aktiviertes Crucifix blockiert genau einen Treffer, auch einen
     * tödlichen. Der nächste Treffer wirkt wieder normal.
     */
    @Test
    public void crucifixBlocksExactlyOneHit() {
        player.addItem(new Item(ItemType.CRUCIFIX, "Crucifix"));

        assertTrue(player.activateCrucifix());
        assertTrue(player.hasCrucifixProtection());
        assertFalse(player.hasItem(ItemType.CRUCIFIX));

        player.takeDamage(1000);
        assertEquals(Player.MAX_HP, player.getHp());
        assertFalse(player.hasCrucifixProtection());

        player.takeDamage(40);
        assertEquals(60, player.getHp());
    }

    /**
     * Ohne Crucifix im Inventar lässt sich kein Schutz aktivieren.
     */
    @Test
    public void crucifixCannotBeActivatedWithoutItem() {
        assertFalse(player.activateCrucifix());
        assertFalse(player.hasCrucifixProtection());
    }


    /**
     * Eine Bandage heilt höchstens bis zu den maximalen HP und wird dabei
     * verbraucht.
     */
    @Test
    public void bandageHealsUpToMaxHpAndIsConsumed() {
        player.takeDamage(10);
        player.addItem(new Item(ItemType.BANDAID, "Bandage"));

        int healed = player.useBandage();

        assertEquals(10, healed);
        assertEquals(Player.MAX_HP, player.getHp());
        assertFalse(player.hasItem(ItemType.BANDAID));
    }

    /**
     * Bei vollen HP bleibt die Bandage im Inventar.
     */
    @Test
    public void bandageIsKeptAtFullHp() {
        player.addItem(new Item(ItemType.BANDAID, "Bandage"));

        assertEquals(0, player.useBandage());
        assertTrue(player.hasItem(ItemType.BANDAID));
    }

    /**
     * Das Maximalgewicht begrenzt, was der Spieler tragen kann.
     */
    @Test
    public void carryLimitBlocksItemsThatAreTooHeavy() {
        for (int i = 0; i < 4; i++) {
            player.addItem(new Item(ItemType.FLASHLIGHT, "Flashlight"));
        }

        assertEquals(12, player.getCarriedWeight());
        assertTrue(player.canCarry(new Item(ItemType.FLASHLIGHT, "Flashlight")));

        player.addItem(new Item(ItemType.FLASHLIGHT, "Flashlight"));

        assertEquals(Player.MAX_WEIGHT, player.getCarriedWeight());
        assertFalse(player.canCarry(new Item(ItemType.KEY, "Key")));
    }

    /**
     * takeItem gibt den Gegenstand zurück und entfernt ihn. Fehlt er,
     * ist das Ergebnis null.
     */
    @Test
    public void takeItemRemovesItemOrReturnsNull() {
        player.addItem(new Item(ItemType.KEY, "Key"));

        Item taken = player.takeItem(ItemType.KEY);

        assertNotNull(taken);
        assertEquals(ItemType.KEY, taken.getType());
        assertFalse(player.hasItem(ItemType.KEY));
        assertNull(player.takeItem(ItemType.KEY));
    }

    /**
     * Coins lassen sich nur ausgeben, wenn genug vorhanden sind. Negative
     * Beträge beim Hinzufügen werden ignoriert.
     */
    @Test
    public void coinsCanOnlyBeSpentIfAvailable() {
        player.addCoins(10);
        player.addCoins(-5);

        assertEquals(10, player.getCoins());
        assertFalse(player.spendCoins(20));
        assertEquals(10, player.getCoins());
        assertTrue(player.spendCoins(4));
        assertEquals(6, player.getCoins());
    }
}