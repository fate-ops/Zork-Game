package ch.bbw.zork;

import java.util.function.LongSupplier;

/**
 * Eine Taschenlampe mit eigenem Ladestand und Ein/Aus-Zustand.
 * Pro 30 Sekunden Leuchtzeit verliert sie 10 Prozentpunkte.
 */
public final class Flashlight extends Item {

    private static final long DRAIN_INTERVAL_NANOS = 30_000_000_000L;

    private final LongSupplier clock;
    private int battery = 100;
    private boolean on;
    private long activeNanos;
    private long lastUpdateNanos;

    /** Erstellt eine voll geladene, ausgeschaltete Taschenlampe. */
    public Flashlight() {
        this(System::nanoTime);
    }

    /**
     * Erstellt eine Taschenlampe mit einer steuerbaren Uhr für Tests.
     *
     * @param clock Zeitquelle in Nanosekunden
     */
    Flashlight(LongSupplier clock) {
        super(ItemType.FLASHLIGHT, "Flashlight");
        this.clock = clock;
    }

    /**
     * Schaltet die Taschenlampe ein, sofern sie geladen ist.
     *
     * @return ob die Taschenlampe eingeschaltet ist
     */
    public boolean turnOn() {
        updateBattery();
        if (battery == 0) {
            return false;
        }

        if (!on) {
            lastUpdateNanos = clock.getAsLong();
            on = true;
        }
        return true;
    }

    /** Schaltet die Taschenlampe aus und pausiert den Verbrauch. */
    public void turnOff() {
        updateBattery();
        on = false;
    }

    /** @return ob die Taschenlampe gerade leuchtet */
    public boolean isOn() {
        updateBattery();
        return on;
    }

    /** @return den aktuellen Ladestand von 0 bis 100 */
    public int getBattery() {
        updateBattery();
        return battery;
    }

    /**
     * Lädt die Taschenlampe bis maximal 100 Prozent nach.
     *
     * @param amount verfügbare Prozentpunkte
     * @return tatsächlich nachgeladene Prozentpunkte
     */
    public int recharge(int amount) {
        updateBattery();
        int charged = Math.min(Math.max(0, amount), 100 - battery);
        battery += charged;
        return charged;
    }

    /** Rechnet die seit dem letzten Aufruf vergangene Leuchtzeit ab. */
    private void updateBattery() {
        if (!on) {
            return;
        }

        long now = clock.getAsLong();
        activeNanos += now - lastUpdateNanos;
        lastUpdateNanos = now;

        long intervals = activeNanos / DRAIN_INTERVAL_NANOS;
        battery = (int) Math.max(0L, battery - intervals * 10L);
        activeNanos %= DRAIN_INTERVAL_NANOS;

        if (battery == 0) {
            on = false;
            activeNanos = 0;
        }
    }
}
