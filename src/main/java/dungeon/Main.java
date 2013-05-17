package dungeon;

import dungeon.messages.Mailman;
import dungeon.ui.Canvas;
import dungeon.ui.InputToMessageConverter;
import dungeon.ui.MainFrame;
import dungeon.ui.SwingMailbox;

/**
 * Main Klasse
 */
public class Main {
	public static void main(String[] args) {
		Log.setLevel(Log.Level.NOTICE); // Hier wird geloggt

		Mailman mailman = new Mailman(); // Der Mailman ...

		MainFrame mainFrame = new MainFrame(mailman); // Erstellt das Hauptfenster welches den Mailman zugewiesen bekommt

		InputToMessageConverter converter = new InputToMessageConverter(mailman); // Dies ist ein KeyListener der die Keys in eine Message umwandelt und an den Mailman schickt
		mainFrame.addKeyListener(converter); // MainFrame bekommt KeyListener (wartet auf Tastatureingaben und bearbeitet sie)

		Canvas canvas = new Canvas(); // Canvas wird erstellt
		mainFrame.add(canvas); // Canvas wird dem MainFrame hinzugefügt

		mailman.addMailbox(new SwingMailbox(mainFrame)); // MainFrame wird dem Mailman hinzugefügt
		mailman.addMailbox(new SwingMailbox(canvas)); // Canvas wird dem Mailman hinzugefügt

		mailman.addHandler(new LevelLoadHandler(mailman)); // ...
		mailman.addHandler(new GameHandler(mailman)); // ...

		mailman.run(); // Mailman wird gestartet
	}
}
