package ch.bbw.zork;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class GameTests {

    @Test(timeout = 5000)
    public void firstDoorStaysLockedWithoutAKey() {
        String output = play("continue", "map");

        assertTrue(output.contains("The door is locked. Search this room for a key."));
        assertTrue(output.contains(">> Door 1 | Items: Key"));
    }

    @Test(timeout = 5000)
    public void unlockingFirstDoorConsumesTheKey() {
        String output = play("search", "continue", "inventory", "map");

        assertTrue(output.contains("You unlocked the door. Your key was used."));
        assertTrue(output.contains("Your inventory is empty."));
        assertTrue(output.contains(">> Door 2 | Items:"));
    }

    @Test(timeout = 5000)
    public void backFromSecondRoomReturnsToTheFirstRoom() {
        String output = play("search", "continue", "peek", "map", "back", "map");

        int secondRoom = output.indexOf(">> Door 2 | Items:");
        int firstRoom = output.indexOf(">> Door 1 | Items:");

        assertTrue(output, secondRoom >= 0);
        assertTrue(output, firstRoom > secondRoom);
    }

    @Test(timeout = 5000)
    public void droppedKeyCanBePickedUpAgain() {
        String output = play("search", "drop key", "map", "search", "inventory");

        assertTrue(output.contains("You dropped: Key"));
        assertTrue(output.contains(">> Door 1 | Items: Key"));
        assertTrue(output.contains("- Key (KEY)"));
        assertTrue(output.contains("Weight: 1/15"));
    }

    @Test(timeout = 5000)
    public void mapListsAllFiftyDoorsAndMarksTheCurrentRoom() {
        String output = play("map");

        for (int door = 1; door <= 50; door++) {
            assertTrue("Map is missing door " + door, output.contains("Door " + door + " | Items:"));
        }

        assertTrue(output.contains(">> Door 1 | Items: Key"));
    }

    private String play(String... commands) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(output)) {
            String script = String.join("\n", commands) + "\nquit\n";
            System.setIn(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
            System.setOut(capture);

            new Game().play();

            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
    }
}