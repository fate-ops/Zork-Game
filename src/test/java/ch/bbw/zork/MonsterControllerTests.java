package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Tests fuer die Klasse MonsterController: Erscheinen, Rush, Screech und
 * Figure. Eine selbst gesteuerte Uhr und ein fester Zufallswert machen
 * die Tests schnell und reproduzierbar.
 */
public class MonsterControllerTests {

    private static final long SECOND = 1_000_000_000L;

    private Player player;
    private long now;

    /**
     * Zufallsquelle, die immer denselben Wert liefert. Der Wert 0 laesst
     * jedes Monster erscheinen, der Wert 99 verhindert alle Begegnungen.
     */
    private static class FixedRandom extends Random {

        private final int value;

        FixedRandom(int value) {
            this.value = value;
        }

        @Override
        public int nextInt(int bound) {
            return value;
        }
    }

    /**
     * Erstellt vor jedem Test einen frischen Spieler und setzt die Uhr
     * auf 0 zurueck.
     */
    @Before
    public void setUp() {
        player = new Player();
        now = 0;
    }

    /**
     * Erstellt einen Controller mit der selbst gesteuerten Uhr.
     */
    private MonsterController controllerWith(int randomValue) {
        return new MonsterController(player, new FixedRandom(randomValue), () -> now);
    }

    /**
     * Erstellt einen Raum mit der gegebenen Nummer, optional dunkel.
     */
    private Room room(int number, boolean dark) {
        Room room = new Room(number, "Test Room", "", 0);

        if (dark) {
            room.makeDark();
        }

        return room;
    }

    /**
     * In Jeffs Shop erscheint nie ein Monster.
     */
    @Test
    public void noMonsterInJeffsShop() {
        MonsterController controller = controllerWith(0);

        controller.onEnter(room(JeffShop.ROOM_NUMBER, true));

        assertFalse(controller.blocksMovement());
    }

    /**
     * Rush erscheint erst ab Raum 5. In einem hellen Raum davor passiert
     * nichts.
     */
    @Test
    public void rushOnlyAppearsFromRoomFive() {
        MonsterController early = controllerWith(0);
        early.onEnter(room(4, false));
        assertFalse(early.blocksMovement());

        MonsterController late = controllerWith(0);
        late.onEnter(room(5, false));
        assertTrue(late.blocksMovement());
    }

    /**
     * Screech erscheint nur in dunklen Raeumen.
     */
    @Test
    public void screechOnlyAppearsInDarkRooms() {
        MonsterController bright = controllerWith(0);
        bright.onEnter(room(3, false));
        assertFalse(bright.blocksMovement());

        MonsterController dark = controllerWith(0);
        dark.onEnter(room(3, true));
        assertTrue(dark.blocksMovement());
    }

    /**
     * Wer sich nicht versteckt, stirbt, sobald die sechs Sekunden von Rush
     * abgelaufen sind.
     */
    @Test
    public void rushKillsPlayerWhoDoesNotHide() {
        MonsterController controller = controllerWith(0);
        controller.onEnter(room(5, false));

        now = 7 * SECOND;

        assertTrue(controller.update());
        assertFalse(player.isAlive());
    }

    /**
     * Wer sich rechtzeitig versteckt, ueberlebt Rush und kann danach mit
     * unhide wieder herauskommen.
     */
    @Test
    public void hidingProtectsFromRush() {
        MonsterController controller = controllerWith(0);
        controller.onEnter(room(5, false));

        assertTrue(controller.handleCommand(new Command("hide")));
        assertTrue(controller.isHidden());

        now = 7 * SECOND;

        assertTrue(controller.update());
        assertTrue(player.isAlive());
        assertTrue(controller.isHidden());

        assertTrue(controller.handleCommand(new Command("unhide")));
        assertFalse(controller.isHidden());
    }
}