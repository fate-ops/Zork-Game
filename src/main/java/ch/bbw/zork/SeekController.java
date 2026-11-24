package ch.bbw.zork;

import java.util.Random;
import java.util.function.LongSupplier;

/**
 * Steuert die Verfolgung durch Seek in den Räumen 35 bis 45. Nach einer
 * kurzen Cutscene muss der Spieler innerhalb eines Zeitlimits bei jeder
 * Tür mit {@code left} oder {@code right} die richtige Seite wählen,
 * die ein {@code *} markiert. Ziel ist Raum 46.
 */
public class SeekController {

    /** Nummer des Raums, in dem die Verfolgung beginnt. */
    public static final int FIRST_ROOM = 35;
    /** Nummer des Raums, der das Ziel der Flucht ist. */
    public static final int ESCAPE_ROOM = 46;

    private static final long SECOND = 1_000_000_000L;
    private static final long CHASE_TIME = 30 * SECOND;
    private static final long WRONG_DOOR_PENALTY = 5 * SECOND;
    private static final long CRUCIFIX_EXTRA_TIME = 10 * SECOND;

    private final Player player;
    private final Random random;
    private final LongSupplier clock;

    private Room currentRoom;

    private boolean intro;
    private boolean chasing;
    private boolean completed;
    private boolean correctLeft;

    private int introStep;
    private long introStarted;
    private long deadline;

    /**
     * Erstellt einen Controller, der die Systemzeit als Uhr verwendet.
     *
     * @param player der Spieler, der gefangen werden kann
     * @param random Zufallsquelle für die richtige Seite
     */
    public SeekController(Player player, Random random) {
        this(player, random, System::nanoTime);
    }

    /**
     * Erstellt einen Controller mit frei wählbarer Uhr. Dieser Konstruktor
     * ist für Tests gedacht, damit Zeitfristen ohne Warten geprüft werden
     * können.
     *
     * @param player der Spieler, der gefangen werden kann
     * @param random Zufallsquelle für die richtige Seite
     * @param clock  Zeitquelle in Nanosekunden
     */
    SeekController(
            Player player,
            Random random,
            LongSupplier clock
    ) {
        this.player = player;
        this.random = random;
        this.clock = clock;
    }

    /**
     * Startet die Verfolgung mit der Cutscene. Wurde Seek schon gestartet
     * oder besiegt, passiert nichts.
     *
     * @param room der Raum, in dem die Verfolgung beginnt (Raum 35)
     */
    public void start(Room room) {
        if (isRunning() || completed) {
            return;
        }

        currentRoom = room;
        intro = true;
        introStep = 0;
        introStarted = clock.getAsLong();

        System.out.println(
                "\nYou step through Door 35. The door slams shut behind you."
        );
        System.out.println(
                "Something moves beneath the black puddles..."
        );
    }

    /**
     * Schaltet die Cutscene weiter und prüft das Zeitlimit. Läuft die Zeit
     * ab, wird der Spieler gefangen, ausser ein Crucifix schützt ihn. Dann
     * erhält er zehn zusätzliche Sekunden.
     *
     * @return {@code true}, wenn dabei eine Meldung ausgegeben wurde
     */
    public boolean update() {
        long now = clock.getAsLong();
        boolean changed = false;

        if (intro) {
            long elapsed = now - introStarted;

            if (introStep == 0 && elapsed >= SECOND) {
                System.out.println(
                        "\nA tall figure rises from the floor."
                );

                introStep = 1;
                changed = true;
            }

            if (introStep == 1 && elapsed >= 2 * SECOND) {
                System.out.println(
                        "\nSeek turns towards you. RUN."
                );

                introStep = 2;
                changed = true;
            }

            if (introStep == 2 && elapsed >= 3 * SECOND) {
                intro = false;
                chasing = true;

                deadline = now + CHASE_TIME;
                correctLeft = random.nextBoolean();

                System.out.println(
                        "\nReach Door 46 in 30 seconds."
                );
                System.out.println(
                        "Use left / right. Follow the *. Wrong door: -5 seconds."
                );

                showRoom();
                changed = true;
            }
        }

        if (chasing && now - deadline >= 0) {
            player.takeDamage(player.getHp());

            if (!player.isAlive()) {
                chasing = false;

                System.out.println("\nSeek caught you");
                System.out.println(
                        "The guiding light shows you where to go with a \"*\", trust it."
                );

            } else {
                // Crucifix blockiert den Fangversuch und gibt zehn Sekunden.
                deadline = clock.getAsLong() + CRUCIFIX_EXTRA_TIME;

                System.out.println(
                        "\nGOT HIM. But why is he not going down?"
                );
                System.out.println(
                        "Seek has been slowed down for 10 Seconds. KEEP RUNNING!"
                );
            }

            changed = true;
        }

        return changed;
    }

    /**
     * Verarbeitet Befehle während der Verfolgung. Mit {@code left} und
     * {@code right} wählt der Spieler eine Tür. Eine falsche Wahl kostet
     * fünf Sekunden, die richtige führt in den nächsten Raum. Befehle
     * wie {@code use}, {@code status}, {@code inventory} und {@code help}
     * sind weiterhin erlaubt, alle anderen werden blockiert.
     *
     * @param command der eingegebene Befehl
     * @return {@code true}, wenn der Befehl hier behandelt wurde und das
     *         Spiel ihn nicht weiter bearbeiten soll
     */
    public boolean handleCommand(Command command) {
        update();

        if (!player.isAlive()) {
            return true;
        }

        if (!isRunning()) {
            return false;
        }

        String word = command.getCommandWord();

        if ("quit".equals(word)) {
            return false;
        }

        if (intro) {
            System.out.println(
                    "Watch out... The chase is about to begin."
            );
            return true;
        }

        if ("look".equals(word)) {
            showRoom();
            return true;
        }

        if ("use".equals(word)
                || "status".equals(word)
                || "inventory".equals(word)
                || "help".equals(word)) {
            return false;
        }

        if (!"left".equals(word) && !"right".equals(word)) {
            System.out.println(
                    "RUN! Choose left or right. Follow the *."
            );
            return true;
        }

        if (command.hasSecondWord()) {
            System.out.println(
                    "Use only 'left' or 'right'."
            );
            return true;
        }

        boolean choseLeft = "left".equals(word);

        if (choseLeft != correctLeft) {
            deadline -= WRONG_DOOR_PENALTY;

            System.out.println(
                    "Wrong door! -5 seconds. You are still in the same room."
            );

            update();

            if (player.isAlive()) {
                showRoom();
            }

            return true;
        }

        currentRoom = currentRoom.nextRoom("forward");

        if (currentRoom.getNumber() == ESCAPE_ROOM) {
            chasing = false;
            completed = true;

            System.out.println(
                    "\nYou reach Door 46 and slam it shut. Seek is gone."
            );
            System.out.println(
                    "The way back has collapsed. Keep moving."
            );

        } else {
            correctLeft = random.nextBoolean();
            showRoom();
        }

        return true;
    }

    /**
     * Zeigt die Seek-Map mit dem {@code *} an der richtigen Seite, die
     * Raumnummer und die verbleibende Zeit.
     */
    private void showRoom() {
        String layout = RoomLayouts.SEEK_RUN
                .replaceFirst(
                        "\\?\\?\\?",
                        correctLeft ? " * " : "   "
                )
                .replaceFirst(
                        "\\?\\?\\?",
                        correctLeft ? "   " : " * "
                );

        System.out.println();
        System.out.println(layout);
        System.out.println(
                "Room: " + currentRoom.getNumber() + " | Goal: Door 46"
        );
        System.out.println(
                "Time left: " + getRemainingSeconds() + " seconds"
        );
        System.out.println("left / right");
    }

    /**
     * Berechnet die verbleibende Zeit der Verfolgung.
     *
     * @return die Restzeit in Sekunden (aufgerundet), 0 wenn keine
     *         Verfolgung läuft
     */
    public long getRemainingSeconds() {
        if (!chasing) {
            return 0;
        }

        long remaining = Math.max(
                0L,
                deadline - clock.getAsLong()
        );

        return (remaining + SECOND - 1) / SECOND;
    }

    /**
     * Gibt an, ob die Verfolgung gerade läuft (Cutscene oder Jagd).
     *
     * @return {@code true}, wenn Seek aktiv ist
     */
    public boolean isRunning() {
        return intro || chasing;
    }

    /**
     * Gibt an, ob der Spieler Seek erfolgreich entkommen ist.
     *
     * @return {@code true}, wenn Raum 46 erreicht wurde
     */
    public boolean hasCompleted() {
        return completed;
    }

    /**
     * Liefert den Raum, in dem sich der Spieler während der Verfolgung
     * befindet.
     *
     * @return der aktuelle Raum, oder {@code null} vor dem Start
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
}