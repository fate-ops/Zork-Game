package ch.bbw.zork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class LibraryPuzzle {

    private static final int PIN_LENGTH = 6;

    private final List<String> symbolOrder;
    private final List<Clue> remainingBooks = new ArrayList<>();
    private final Map<String, Integer> notes = new LinkedHashMap<>();
    private final String correctPin;

    public LibraryPuzzle(Random random) {
        List<String> symbols = new ArrayList<>(Arrays.asList(
                "MOON", "EYE", "STAR", "KEY", "CROWN", "FLAME", "DIAMOND", "CROSS"
        ));
        Collections.shuffle(symbols, random);
        symbolOrder = new ArrayList<>(symbols.subList(0, PIN_LENGTH));

        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < symbols.size(); i++) {
            int digit = random.nextInt(10);
            remainingBooks.add(new Clue(symbols.get(i), digit));
            if (i < PIN_LENGTH) {
                pin.append(digit);
            }
        }
        correctPin = pin.toString();

        Collections.shuffle(remainingBooks, random);
    }

    public void showSource() {
        System.out.println("PIN order: " + String.join(" -> ", symbolOrder));
    }

    public boolean findBook() {
        if (remainingBooks.isEmpty()) {
            System.out.println("You have already found all eight books. Check your notes.");
            return false;
        }

        Clue clue = remainingBooks.remove(remainingBooks.size() - 1);
        notes.put(clue.symbol, clue.digit);
        System.out.println("You found a book: " + clue.symbol + " = " + clue.digit);
        return true;
    }

    public void showNotes() {
        if (notes.isEmpty()) {
            System.out.println("You haven't found any clues yet.");
            return;
        }

        System.out.println("Your notes:");
        for (Map.Entry<String, Integer> clue : notes.entrySet()) {
            System.out.println(clue.getKey() + " = " + clue.getValue());
        }
    }

    public boolean isValidPinFormat(String attempt) {
        return attempt != null && attempt.matches("[0-9]{" + PIN_LENGTH + "}");
    }

    public boolean tryCode(String attempt) {
        if (!isValidPinFormat(attempt)) {
            System.out.println("Use: code <six digits>. Include any leading zero.");
            return false;
        }

        if (!correctPin.equals(attempt)) {
            System.out.println("The lock clicks. The PIN is wrong.");
            return false;
        }

        System.out.println("The Great Gate unlocks.");
        System.out.println("You escaped the hotel!");
        return true;
    }

    private static final class Clue {
        private final String symbol;
        private final int digit;

        private Clue(String symbol, int digit) {
            this.symbol = symbol;
            this.digit = digit;
        }
    }
}