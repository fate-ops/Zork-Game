package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests fuer die Klasse Room, Schwerpunkt dunkle Raeume.
 */
public class RoomTests {

    private Room room;

    @Before
    public void setUp() {
        room = new Room("Test Room");
    }

    @Test
    public void newRoomIsNotDark() {
        assertFalse(room.isDark());
    }

    @Test
    public void makeDarkMakesRoomDark() {
        room.makeDark();
        assertTrue(room.isDark());
    }

    @Test
    public void darkRoomDescriptionWarnsAboutDarkness() {
        room.makeDark();
        assertTrue(room.longDescription().contains("dark"));
    }

    @Test
    public void lightRoomDescriptionHasNoDarknessWarning() {
        assertFalse(room.longDescription().contains("dark"));
    }

    @Test
    public void darkRoomStillContainsItems() {
        room.makeDark();
        room.addItem(new Item(ItemType.FLASHLIGHT, "Flashlight"));

        List<Item> items = room.collectItems();

        assertEquals(1, items.size());
        assertEquals(ItemType.FLASHLIGHT, items.get(0).getType());
    }

    @Test
    public void makeDarkDoesNotLockRoom() {
        room.makeDark();
        assertFalse(room.isLocked());
    }
}