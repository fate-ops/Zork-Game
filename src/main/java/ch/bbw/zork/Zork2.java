package ch.bbw.zork;

/**
 * Einstiegspunkt des Spiels DOORS: Terminal Edition. Die Klasse erstellt
 * ein {@link Game} und startet es.
 */
public class Zork2 {

	/**
	 * Startet das Spiel.
	 *
	 * @param args Kommandozeilenargumente, werden nicht verwendet
	 */
	public static void main(String[] args) {
		Game zorkgame = new Game();
		zorkgame.play();
	}

}