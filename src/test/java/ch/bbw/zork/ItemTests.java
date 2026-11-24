package ch.bbw.zork;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests fuer die Klasse Item: Typ, Name und Gewicht.
 */
public class ItemTests {

    /**
     * Ein Item speichert den Typ und den Namen, mit denen es erstellt wurde.
     */
    @Test
    public void itemKeepsTypeAndName() {
        Item item = new Item(ItemType.KEY, "Key");

        assertEquals(ItemType.KEY, item.getType());
        assertEquals("Key", item.getName());
    }

    /**
     * Die Flashlight ist mit Gewicht 3 der schwerste Gegenstand.
     */
    @Test
    public void flashlightWeighsThree() {
        Item item = new Item(ItemType.FLASHLIGHT, "Flashlight");

        assertEquals(3, item.getWeight());
    }

    /**
     * Batterie und Crucifix wiegen je 2.
     */
    @Test
    public void batteryAndCrucifixWeighTwo() {
        assertEquals(2, new Item(ItemType.BATTERY, "Battery").getWeight());
        assertEquals(2, new Item(ItemType.CRUCIFIX, "Crucifix").getWeight());
    }

    /**
     * Key, Lockpick und Bandage wiegen je 1.
     */
    @Test
    public void lightItemsWeighOne() {
        assertEquals(1, new Item(ItemType.KEY, "Key").getWeight());
        assertEquals(1, new Item(ItemType.LOCKPICK, "Lockpick").getWeight());
        assertEquals(1, new Item(ItemType.BANDAID, "Bandage").getWeight());
    }

    /**
     * Jeder Gegenstandstyp hat ein positives Gewicht, das nicht grösser
     * als das Maximalgewicht des Spielers ist. So kann jeder Gegenstand
     * grundsätzlich getragen werden.
     */
    @Test
    public void everyItemTypeHasUsableWeight() {
        for (ItemType type : ItemType.values()) {
            int weight = new Item(type, type.name()).getWeight();

            assertTrue(type + " muss ein positives Gewicht haben", weight > 0);
            assertTrue(type + " darf nicht schwerer als MAX_WEIGHT sein",
                    weight <= Player.MAX_WEIGHT);
        }
    }
}