package ch.bbw.zork;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Tests fuer die Klasse LibraryPuzzle. Der PIN ist privat, deshalb loesen
 * die Tests das Raetsel wie ein Spieler: Sie lesen die Ausgabe von
 * showSource und findBook und setzen daraus den PIN zusammen.
 */
public class LibraryPuzzleTests {

    private LibraryPuzzle puzzle;

    /**
     * Erstellt vor jedem Test ein Raetsel mit festem Zufalls-Startwert,
     * damit die Tests reproduzierbar sind.
     */
    @Before
    public void setUp() {
        puzzle = new LibraryPuzzle(new Random(42));
    }

    /**
     * Faengt die Konsolenausgabe einer Aktion ab und gibt sie als Text
     * zurueck.
     */
    private String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true));

        try {
            action.run();
        } finally {
            System.setOut(original);
        }

        return buffer.toString();
    }

    /**
     * Loest das Raetsel: liest die Symbolreihenfolge, findet alle acht
     * Buecher und setzt den richtigen PIN zusammen.
     */
    private String solvePin(LibraryPuzzle target) {
        String order = capture(target::showSource);
        String[] symbols = order.replace("PIN order:", "")
                .trim()
                .split("\\s*->\\s*");

        Map<String, Integer> digits = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            String output = capture(target::findBook);
            String clue = output.substring(output.indexOf(':') + 1).trim();
            String[] parts = clue.split("\\s*=\\s*");

            digits.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }

        StringBuilder pin = new StringBuilder();

        for (String symbol : symbols) {
            pin.append(digits.get(symbol));
        }

        return pin.toString();
    }

    /**
     * Ein gueltiger PIN besteht aus genau sechs Ziffern, fuehrende Nullen
     * sind erlaubt.
     */
    @Test
    public void pinFormatAcceptsExactlySixDigits() {
        assertTrue(puzzle.isValidPinFormat("123456"));
        assertTrue(puzzle.isValidPinFormat("000123"));
    }

    /**
     * Zu kurze, zu lange, leere und nicht numerische Eingaben sind kein
     * gueltiger PIN.
     */
    @Test
    public void pinFormatRejectsInvalidInput() {
        assertFalse(puzzle.isValidPinFormat("12345"));
        assertFalse(puzzle.isValidPinFormat("1234567"));
        assertFalse(puzzle.isValidPinFormat("12a456"));
        assertFalse(puzzle.isValidPinFormat(""));
        assertFalse(puzzle.isValidPinFormat(null));
    }

    /**
     * Es gibt genau acht Buecher. Danach findet der Spieler keine mehr.
     */
    @Test
    public void exactlyEightBooksCanBeFound() {
        for (int i = 0; i < 8; i++) {
            assertTrue("Buch " + (i + 1) + " muss gefunden werden",
                    capture(puzzle::findBook).contains("You found a book"));
        }

        assertFalse(puzzle.findBook());
    }

    /**
     * Ein Code mit falschem Format wird abgelehnt.
     */
    @Test
    public void codeWithWrongFormatIsRejected() {
        assertFalse(puzzle.tryCode("123"));
        assertFalse(puzzle.tryCode(null));
    }

    /**
     * Ein korrekt formatierter, aber falscher PIN oeffnet das Tor nicht.
     */
    @Test
    public void wrongPinDoesNotOpenGate() {
        String pin = solvePin(puzzle);
        int lastDigit = pin.charAt(5) - '0';
        String wrongPin = pin.substring(0, 5) + ((lastDigit + 1) % 10);

        assertFalse(puzzle.tryCode(wrongPin));
    }

    /**
     * Der aus den Hinweisen zusammengesetzte PIN oeffnet das Great Gate.
     */
    @Test
    public void solvedPinOpensGate() {
        String pin = solvePin(puzzle);

        assertEquals(6, pin.length());
        assertTrue(puzzle.tryCode(pin));
    }

    /**
     * Zwei Raetsel mit demselben Zufalls-Startwert haben dieselbe Loesung.
     */
    @Test
    public void sameSeedGivesSameSolution() {
        LibraryPuzzle other = new LibraryPuzzle(new Random(42));

        assertEquals(solvePin(puzzle), solvePin(other));
    }
}