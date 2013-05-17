package dungeon.ui;

import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.messages.Mailman;
import dungeon.messages.LifecycleEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main frame for the application.
 * <p/>
 * handleMessage() will be called on the swing EDT. This means that any methods called by handleMessage() are generally
 * save to manipulate the frame.
 *
 * Diese Klasse ist die Fensterklasse vom Typ JFrame.
 */
public class MainFrame extends JFrame implements MessageHandler {
	public static final String TITLE = "DUNGEON GAME"; // Titel von des MainFrames

	private final Mailman mailman;

	public MainFrame(Mailman mailman) {
		this.mailman = mailman; // Mailman für die Klasse verfügbar machen
	}

	/**
	 * MessageHandler-Funktion
	 * Wird vermutlich vom Mailman verwaltet ...
	 */
	@Override
	public void handleMessage(Message message) {
		if (message == LifecycleEvent.INITIALIZE) { // Wenn INITIALIZE, dann Fenster initialisieren und anzeigen
			this.initialize();
		} else if (message == LifecycleEvent.SHUTDOWN) { // Wenn SHUTDOWN, dann Fenster entfernen
			this.dispose();
		}
	}

	private void initialize() {
		this.setName(TITLE);  // Name wird gesetzt
		this.setTitle(TITLE); // Titel wird gesetzt

		this.setUndecorated(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setResizable(false);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainFrame.this.mailman.send(LifecycleEvent.SHUTDOWN); // Wenn das Fenster geschlossen wird, Message an Mailman schicken
			}
		});

		this.setVisible(true); // MainFrame sichtbar machen
	}
}
