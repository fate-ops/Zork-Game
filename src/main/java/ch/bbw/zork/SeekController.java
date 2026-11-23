package ch.bbw.zork;

import java.util.Random;
import java.util.function.LongSupplier;

public class SeekController {

    public static final int FIRST_ROOM = 35;
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

    public SeekController(Player player, Random random) {
        this(player, random, System::nanoTime);
    }

    SeekController(
            Player player,
            Random random,
            LongSupplier clock
    ) {
        this.player = player;
        this.random = random;
        this.clock = clock;
    }

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

    public boolean isRunning() {
        return intro || chasing;
    }

    public boolean hasCompleted() {
        return completed;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
}