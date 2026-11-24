package ch.bbw.diverses;

import java.util.Stack;

/**
 * Kleines Beispiel aus dem Unterricht, das den Umgang mit einem
 * {@link Stack} zeigt. Die Klasse gehört nicht zum eigentlichen Spiel.
 */
public class TestStack {

	/**
	 * Legt drei Texte auf einen Stack und gibt den obersten wieder aus.
	 *
	 * @param args Kommandozeilenargumente, werden nicht verwendet
	 */
	public static void main(String[] args) {

		Stack<String> stack = new Stack<>();

		stack.push("Hello");
		stack.push("World");
		stack.push("how are you");

		String cmd = stack.pop();
		System.out.println(cmd);
	}

}