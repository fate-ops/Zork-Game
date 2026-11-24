package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class FlashlightTests {

    private static final long SECOND = 1_000_000_000L;
    private AtomicLong clock;
    private Flashlight flashlight;

    @Before
    public void setUp() {
        clock = new AtomicLong();
        flashlight = new Flashlight(clock::get);
    }

    @Test
    public void startsChargedAndTurnedOff() {
        Item item = flashlight;

        assertEquals(ItemType.FLASHLIGHT, item.getType());
        assertEquals(3, item.getWeight());
        assertEquals(100, flashlight.getBattery());
        assertFalse(flashlight.isOn());
    }

    @Test
    public void drainsOnlyAfterThirtySecondsOfUse() {
        assertTrue(flashlight.turnOn());
        advanceSeconds(29);
        assertEquals(100, flashlight.getBattery());

        advanceSeconds(1);
        assertEquals(90, flashlight.getBattery());
        assertTrue(flashlight.isOn());
    }

    @Test
    public void switchingOffPausesDrainAndKeepsPartialUsage() {
        assertTrue(flashlight.turnOn());
        advanceSeconds(15);
        flashlight.turnOff();

        advanceSeconds(90);
        assertFalse(flashlight.isOn());
        assertEquals(100, flashlight.getBattery());

        assertTrue(flashlight.turnOn());
        advanceSeconds(15);
        assertEquals(90, flashlight.getBattery());
    }

    @Test
    public void emptyBatteryShutsDownAndChargingIsCapped() {
        assertTrue(flashlight.turnOn());
        advanceSeconds(300);

        assertFalse(flashlight.isOn());
        assertEquals(0, flashlight.getBattery());
        assertFalse(flashlight.turnOn());

        assertEquals(30, flashlight.recharge(30));
        assertEquals(70, flashlight.recharge(90));
        assertEquals(100, flashlight.getBattery());
        assertEquals(0, flashlight.recharge(10));
        assertTrue(flashlight.turnOn());
    }

    @Test
    public void removingTheFlashlightTurnsItOffAndKeepsItsCharge() {
        Player player = new Player();
        player.addItem(flashlight);
        assertTrue(player.turnFlashlightOn());
        advanceSeconds(30);

        Item dropped = player.takeItem(ItemType.FLASHLIGHT);
        assertSame(flashlight, dropped);
        advanceSeconds(60);
        player.addItem(dropped);

        assertFalse(player.isFlashlightOn());
        assertEquals(90, player.getFlashlightBattery());
        assertEquals(100, new Flashlight(clock::get).getBattery());
    }

    private void advanceSeconds(long seconds) {
        clock.addAndGet(seconds * SECOND);
    }
}
