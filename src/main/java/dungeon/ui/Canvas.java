package dungeon.ui;

import dungeon.LevelLoadHandler;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.*;
import dungeon.models.messages.Transform;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Canvas extends JPanel implements MessageHandler {
	private final Color blockingTile = new Color(181, 125, 147); // z.B. Wandfarbe

	private final Color passableTile = new Color(255, 207, 242); // z.B. Bodenfarbe

	private final Color playerColor = new Color(101, 202, 227); // Spielerfarbe

	private final Color enemyColor = new Color(33, 237, 60); // Gegnerfarbe

	private World world;

	Image image;
	Map<String, Image> imageMap = new HashMap<String, Image>();
	public Canvas() {
		try {
			imageMap.put("wall", getToolkit().getImage(getClass().getResource("/wand.jpg")));
			imageMap.put("enemy", getToolkit().getImage(getClass().getResource("/schwein.png")));
			imageMap.put("player", getToolkit().getImage(getClass().getResource("/spieler.png")));
		}
		catch (Exception e) {}
	}

	@Override
	public void handleMessage(Message message) {
		if (message instanceof Transform) {
			this.world = this.world.apply((Transform) message);
		} else if (message instanceof LevelLoadHandler.LevelLoadedEvent) {
			this.world = ((LevelLoadHandler.LevelLoadedEvent) message).getWorld();
		}

		repaint();
	}

	/**
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.world == null) {
			return;
		}

		int blockSize = (int)(g.getClipBounds().width / 150) * 10;

		Room room = this.world.getCurrentRoom();

		g.setColor(Color.black);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

		//int tileWidth = g.getClipBounds().width / room.getSize();
		//int tileHeight = g.getClipBounds().height / room.getSize();
		int row = 0;

		for (List<Tile> tiles : room.getTiles()) {
			int column = 0;

			for (Tile tile : tiles) {
				if (tile.isBlocking()) {
					//g.setColor(this.blockingTile);
					g.drawImage(imageMap.get("wall"), blockSize + column * blockSize, blockSize + row * blockSize, blockSize, blockSize, this);
				} else {
					g.setColor(this.passableTile);
					g.fillRect(blockSize + column * blockSize, blockSize + row * blockSize, blockSize, blockSize);
				}

				//g.fillRect(column * tileWidth, row * tileHeight, tileWidth, tileHeight);

				column++;
			}

			row++;
		}

		for (Enemy enemy : room.getEnemies()) {
			Position position = enemy.getPosition();

			g.setColor(this.enemyColor);
			//g.fillRect((int) (position.getX() * tileWidth), (int) (position.getY() * tileHeight), tileWidth, tileHeight);
			g.drawImage(imageMap.get("enemy"), (int) (blockSize + position.getX() * blockSize), blockSize + (int) (position.getY() * blockSize), blockSize, blockSize, this);
			//g.fillRect((int) (4 * blockSize + position.getX() * blockSize), blockSize + (int) (position.getY() * blockSize), blockSize, blockSize);
		}

		Position playerPosition = this.world.getPlayer().getPosition();

		g.setColor(this.playerColor);
		//g.fillRect((int) (playerPosition.getX() * tileWidth), (int) (playerPosition.getY() * tileHeight), tileWidth, tileHeight);
		//g.fillRect((int) (4 * blockSize + playerPosition.getX() * blockSize), blockSize + (int) (playerPosition.getY() * blockSize), blockSize, blockSize);
		g.drawImage(imageMap.get("player"), (int) (blockSize + playerPosition.getX() * blockSize), blockSize + (int) (playerPosition.getY() * blockSize), blockSize, blockSize, this);
	}
}
