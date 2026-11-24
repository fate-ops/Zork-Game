package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests fuer die Klasse JeffShop: Preise, Kaeufe und Fehlerfaelle.
 */
public class JeffShopTests {

    private JeffShop shop;
    private Player player;

    /**
     * Erstellt vor jedem Test einen frischen Shop und einen frischen
     * Spieler.
     */
    @Before
    public void setUp() {
        shop = new JeffShop();
        player = new Player();
    }

    /**
     * Ein Crucifix kostet 250 Coins und landet im Inventar.
     */
    @Test
    public void buyingCrucifixCostsTwoHundredFiftyCoins() {
        player.addCoins(300);

        shop.buy("crucifix", player);

        assertTrue(player.hasItem(ItemType.CRUCIFIX));
        assertEquals(50, player.getCoins());
    }

    /**
     * Ein Lockpick kostet 50 Coins.
     */
    @Test
    public void buyingLockpickCostsFiftyCoins() {
        player.addCoins(50);

        shop.buy("lockpick", player);

        assertTrue(player.hasItem(ItemType.LOCKPICK));
        assertEquals(0, player.getCoins());
    }

    /**
     * Reichen die Coins nicht aus, wird weder Geld abgezogen noch ein Item
     * vergeben.
     */
    @Test
    public void notEnoughCoinsMeansNoPurchase() {
        player.addCoins(49);

        shop.buy("lockpick", player);

        assertFalse(player.hasItem(ItemType.LOCKPICK));
        assertEquals(49, player.getCoins());
    }

    /**
     * Ein unbekanntes Produkt verändert weder Coins noch Inventar.
     */
    @Test
    public void unknownProductChangesNothing() {
        player.addCoins(500);

        shop.buy("pizza", player);

        assertEquals(500, player.getCoins());
        assertEquals(0, player.getCarriedWeight());
    }

    /**
     * Ist das Inventar zu schwer, wird nichts verkauft und nichts bezahlt.
     */
    @Test
    public void tooHeavyInventoryBlocksPurchase() {
        for (int i = 0; i < 5; i++) {
            player.addItem(new Item(ItemType.FLASHLIGHT, "Flashlight"));
        }
        player.addCoins(100);

        shop.buy("lockpick", player);

        assertFalse(player.hasItem(ItemType.LOCKPICK));
        assertEquals(100, player.getCoins());
    }

    /**
     * Eine gekaufte Flashlight ersetzt die alte: Das Gewicht bleibt bei
     * einer Flashlight, der Akku ist voll und die Lampe ausgeschaltet.
     */
    @Test
    public void boughtFlashlightReplacesTheOldOne() {
        player.addItem(new Item(ItemType.FLASHLIGHT, "Flashlight"));
        player.addCoins(100);

        shop.buy("flashlight", player);

        assertEquals(0, player.getCoins());
        assertEquals(3, player.getCarriedWeight());
        assertEquals(100, player.getFlashlightBattery());
        assertFalse(player.isFlashlightOn());
    }
}