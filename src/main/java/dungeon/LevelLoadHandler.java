package dungeon;

import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.messages.Mailman;
import dungeon.messages.LifecycleEvent;
import dungeon.models.*;

import java.util.Arrays;

/**
 * LevelLoadHandler
 * Zuständig für das Erstellen des Levels(Karte)
 */
public class LevelLoadHandler implements MessageHandler {
	private final Mailman mailman;

	public LevelLoadHandler(Mailman mailman) {
		this.mailman = mailman; // Mailman für Klasse verfügbar machen
	}

	@Override
	public void handleMessage(Message message) {
		if (message == LifecycleEvent.INITIALIZE) { // Wenn INITIALIZE, dann Level(Karte) erstellen
			World world = new World(
					Arrays.asList(
							new Room(
									"warm-up",
									Arrays.asList(
											new Enemy(new Position(1, 1)) // Gegner
									),
									Arrays.asList( // Raumblöcke
											Arrays.asList(new Tile(true), new Tile(true), new Tile(true), new Tile(true), new Tile(true)),
											Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
											Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
											Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
											Arrays.asList(new Tile(false), new Tile(false), new Tile(false), new Tile(false))
									)
							)
					),
					new Player("Link", 1, "warm-up", new Position(0, 0)) // Spielder
			);

			this.mailman.send(new LevelLoadedEvent(world));
		}
	}

	/**
	 * LevelLoadEvent Klasse
	 * @todo: Klasse in Klasse nicht sinnvoll!!!!
	 */
	public static class LevelLoadedEvent implements Message {
		private final World world;

		public LevelLoadedEvent(World world) {
			this.world = world;
		}

		public World getWorld() {
			return this.world;
		}
	}
}
