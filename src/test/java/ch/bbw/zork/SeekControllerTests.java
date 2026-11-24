package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class SeekControllerTests {

    private static final long SECOND = 1_000_000_000L;

    private AtomicLong clock;
    private Player player;
    private SeekController seek;

    @Before
    public void setUp() {
        clock = new AtomicLong();
        player = new Player();
        seek = new SeekController(player, new Random(0) {
            @Override
            public boolean nextBoolean() {
                return true;
            }
        }, clock::get);

        Room first = new Room(SeekController.FIRST_ROOM, "Seek", "", 0);
        Room previous = first;

        for (int number = SeekController.FIRST_ROOM + 1;
             number <= SeekController.ESCAPE_ROOM; number++) {
            Room next = new Room(number, "Seek", "", 0);
            previous.setExit("forward", next);
            previous = next;
        }

        seek.start(first);
    }

    @Test
    public void introBlocksMovementAndThenStartsTheTimer() {
        assertTrue(seek.handleCommand(new Command("left")));
        assertEquals(SeekController.FIRST_ROOM, seek.getCurrentRoom().getNumber());
        assertEquals(0L, seek.getRemainingSeconds());

        startChase();

        assertEquals(30L, seek.getRemainingSeconds());
    }

    @Test
    public void choosingTheWrongDoorCostsFiveSecondsWithoutMoving() {
        startChase();

        assertTrue(seek.handleCommand(new Command("right")));
        assertEquals(25L, seek.getRemainingSeconds());
        assertEquals(SeekController.FIRST_ROOM, seek.getCurrentRoom().getNumber());
    }

    @Test
    public void choosingTheCorrectDoorsCompletesTheChase() {
        startChase();

        int doors = SeekController.ESCAPE_ROOM - SeekController.FIRST_ROOM;
        for (int i = 0; i < doors; i++) {
            assertTrue(seek.handleCommand(new Command("left")));
        }

        assertEquals(SeekController.ESCAPE_ROOM, seek.getCurrentRoom().getNumber());
        assertTrue(seek.hasCompleted());
        assertFalse(seek.isRunning());
        assertTrue(player.isAlive());
    }

    @Test
    public void runningOutOfTimeEndsTheChaseWithoutProtection() {
        startChase();

        clock.addAndGet(30 * SECOND);
        assertTrue(seek.update());

        assertFalse(player.isAlive());
        assertFalse(seek.isRunning());
        assertFalse(seek.hasCompleted());
    }

    @Test
    public void crucifixBuysTenSecondsAndIsConsumed() {
        player.addItem(new Item(ItemType.CRUCIFIX, "Crucifix"));
        assertTrue(player.activateCrucifix());
        startChase();

        clock.addAndGet(30 * SECOND);
        assertTrue(seek.update());

        assertTrue(player.isAlive());
        assertTrue(seek.isRunning());
        assertFalse(player.hasCrucifixProtection());
        assertEquals(10L, seek.getRemainingSeconds());
    }

    private void startChase() {
        clock.addAndGet(3 * SECOND);
        assertTrue(seek.update());
    }
}