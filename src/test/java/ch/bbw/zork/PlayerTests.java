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
}