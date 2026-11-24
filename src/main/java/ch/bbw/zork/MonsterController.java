package ch.bbw.zork;

import java.util.Random;
import java.util.function.LongSupplier;

/**
 * Steuert die Monster Rush, Screech und Figure. Die Klasse würfelt das
 * Erscheinen beim Betreten eines Raums, überwacht die Reaktionsfristen
 * und verarbeitet die passenden Befehle wie {@code hide}, {@code unhide}
 * und {@code peek}. Seek wird von {@link SeekController} gesteuert.
 */
public class MonsterController {

    private static final long RUSH_TIME = 6_000_000_000L;
    private static final long SCREECH_TIME = 3_000_000_000L;
    private static final long FIGURE_TIME = 4_000_000_000L;

    private final Player player;
    private final Random random;
    private final LongSupplier clock;

    private boolean rushActive;
    private boolean screechActive;
    private boolean hidden;
    private boolean figureActive;

    private long deadline;

    /**
     * Erstellt einen Controller, der die Systemzeit als Uhr verwendet.
     *
     * @param player der Spieler, der Schaden erleiden kann
     * @param random Zufallsquelle für das Erscheinen der Monster
     */
    public MonsterController(Player player, Random random) {
        this(player, random, System::nanoTime);
    }

    /**
     * Erstellt einen Controller mit frei wählbarer Uhr. Dieser Konstruktor
     * ist für Tests gedacht, damit Zeitfristen ohne Warten geprüft werden
     * können.
     *
     * @param player der Spieler, der Schaden erleiden kann
     * @param random Zufallsquelle für das Erscheinen der Monster
     * @param clock  Zeitquelle in Nanosekunden
     */
    MonsterController(
            Player player,
            Random random,
            LongSupplier clock
    ) {
        this.player = player;
        this.random = random;
        this.clock = clock;
    }

    /**
     * Prüft beim Betreten eines Raums, ob ein Monster erscheint. Ab Raum 5
     * wird zuerst Rush gewürfelt, danach in dunklen Räumen Screech. In
     * Jeffs Shop und während einer laufenden Begegnung passiert nichts.
     *
     * @param room der betretene Raum
     */
    public void onEnter(Room room) {
        if (room.getNumber() == JeffShop.ROOM_NUMBER
                || rushActive
                || screechActive
                || hidden) {
            return;
        }

        if (room.getNumber() >= 5 && random.nextInt(100) < 25) {
            rushActive = true;
            deadline = clock.getAsLong() + RUSH_TIME;

            System.out.println("flicker flicker");

        } else if (room.isDark() && random.nextInt(100) < 50) {
            screechActive = true;
            deadline = clock.getAsLong() + SCREECH_TIME;

            System.out.println("psst psst");
        }
    }

    /**
     * Startet eine Begegnung mit Figure. Der Spieler hat vier Sekunden Zeit,
     * sich mit {@code hide} zu verstecken.
     */
    public void warnFigure() {
        figureActive = true;
        deadline = clock.getAsLong() + FIGURE_TIME;
        System.out.println("Figure heard you! Type 'hide' within 4 seconds!");
    }

    /**
     * Prüft, ob eine Reaktionsfrist abgelaufen ist, und wertet die
     * Begegnung aus. Der Spieler nimmt Schaden, wenn er nicht rechtzeitig
     * reagiert hat. Ein Crucifix kann einen tödlichen Treffer verhindern.
     *
     * @return {@code true}, wenn eine Begegnung abgeschlossen wurde und
     *         dabei eine Meldung ausgegeben wurde
     */
    public boolean update() {
        long now = clock.getAsLong();

        if (rushActive && now - deadline >= 0) {
            rushActive = false;

            System.out.println();

            if (hidden) {
                System.out.println(
                        "A dark force just rushed through. I should probably move"
                );

            } else {
                player.takeDamage(player.getHp());

                if (!player.isAlive()) {
                    System.out.println("Oh, looks like rush got you.");
                    System.out.println(
                            "Next time, try to hide quick enough.."
                    );

                } else {
                    System.out.println("OH MAN. CLOSE ONE");
                    System.out.println("Rush has been crucified.");
                }
            }

            return true;
        }

        if (screechActive && now - deadline >= 0) {
            screechActive = false;

            System.out.println();

            int previousHp = player.getHp();
            player.takeDamage(40);

            if (!player.isAlive()) {
                System.out.println("Oh, you died to screech.");
                System.out.println(
                        "Just peek at him and this scaredy cat will leave you alone.."
                );

            } else if (player.getHp() < previousHp) {
                System.out.println(
                        "OUCH. Stupid little crap. I better get out of here"
                );

            } else {
                System.out.println("GO AWAY!");
                System.out.println("Screech has been crucified");
            }

            return true;
        }

        if (figureActive && now - deadline >= 0) {
            figureActive = false;
            System.out.println();

            if (hidden) {
                System.out.println("Figure walked past. You can 'unhide' now.");
            } else {
                player.takeDamage(player.getHp());

                if (player.isAlive()) {
                    System.out.println("Your crucifix blocked Figure's attack.");
                } else {
                    System.out.println("Figure caught you.");
                    System.out.println("Next time, hide as soon as you hear him coming.");
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Verarbeitet Befehle, die zu einer Monsterbegegnung gehören
     * ({@code hide}, {@code unhide} und {@code peek}). Im Versteck ist nur
     * {@code unhide} erlaubt, und während Figure naht, ist nur
     * {@code hide} möglich.
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

        String word = command.getCommandWord();

        if (hidden) {
            if (!"unhide".equals(word)) {
                System.out.println(
                        "You are hiding. Only 'unhide' is allowed."
                );

            } else if (command.hasSecondWord()) {
                System.out.println(
                        "Use 'unhide' without another word."
                );

            } else if (rushActive || figureActive) {
                System.out.println(
                        "Not yet. Stay hidden until the danger has passed."
                );

            } else {
                hidden = false;

                System.out.println(
                        "You step out of your hiding place."
                );
            }

            return true;
        }

        if ("hide".equals(word)) {
            if (command.hasSecondWord()) {
                System.out.println(
                        "Use 'hide' without another word."
                );

            } else if (rushActive || figureActive) {
                hidden = true;

                System.out.println(
                        "You hide. Stay here until the danger has passed."
                );

            } else {
                System.out.println(
                        "There is nothing to hide from right now."
                );
            }

            return true;
        }

        if ("unhide".equals(word)) {
            System.out.println("You are not hiding.");
            return true;
        }

        if (figureActive) {
            System.out.println("Figure is coming. Type 'hide'!");
            return true;
        }

        if ("peek".equals(word)) {
            if (command.hasSecondWord()) {
                System.out.println(
                        "Use 'peek' without another word."
                );

            } else if (screechActive) {
                screechActive = false;

                System.out.println(
                        "What the hell was that? Whatever."
                );

            } else {
                System.out.println(
                        "You look around. Nothing is there."
                );
            }

            return true;
        }

        return false;
    }

    /**
     * Gibt an, ob sich der Spieler gerade versteckt.
     *
     * @return {@code true}, wenn der Spieler versteckt ist
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Gibt an, ob eine Begegnung oder das Verstecken den Raumwechsel
     * sperrt.
     *
     * @return {@code true}, wenn der Spieler den Raum nicht wechseln darf
     */
    public boolean blocksMovement() {
        return rushActive || screechActive || figureActive || hidden;
    }
}