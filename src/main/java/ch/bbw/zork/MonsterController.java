package ch.bbw.zork;

import java.util.Random;
import java.util.function.LongSupplier;

public class MonsterController {

    private static final long RUSH_TIME = 6_000_000_000L;
    private static final long SCREECH_TIME = 3_000_000_000L;

    private final Player player;
    private final Random random;
    private final LongSupplier clock;

    private boolean rushActive;
    private boolean screechActive;
    private boolean hidden;

    private long deadline;

    public MonsterController(Player player, Random random) {
        this(player, random, System::nanoTime);
    }

    MonsterController(
            Player player,
            Random random,
            LongSupplier clock
    ) {
        this.player = player;
        this.random = random;
        this.clock = clock;
    }

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

        return false;
    }

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

            } else if (rushActive) {
                System.out.println(
                        "Not yet. Stay hidden until Rush has passed."
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

            } else if (rushActive) {
                hidden = true;

                System.out.println(
                        "You hide. Stay here until Rush has passed."
                );

            } else {
                System.out.println(
                        "There is no approaching Rush to hide from."
                );
            }

            return true;
        }

        if ("unhide".equals(word)) {
            System.out.println("You are not hiding.");
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

    public boolean isHidden() {
        return hidden;
    }

    public boolean blocksMovement() {
        return rushActive || screechActive;
    }
}