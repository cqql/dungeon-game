package dungeon.ui;

import dungeon.LevelLoadHandler;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.*;
import dungeon.models.messages.Transform;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;

public class Canvas extends JPanel implements MessageHandler {
  private final Color blockingTile = new Color(181, 125, 147);

  private final Color passableTile = new Color(139, 108, 217);

  private final Color playerColor = new Color(101, 202, 227);

  private final Color enemyColor = new Color(33, 237, 60);

  private World world;

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world = this.world.apply((Transform) message);
    } else if (message instanceof LevelLoadHandler.LevelLoadedEvent) {
      this.world = ((LevelLoadHandler.LevelLoadedEvent) message).getWorld();
    }

    repaint();
  }

  @Override
  protected void paintComponent (Graphics g) {
    super.paintComponent(g);

    if (this.world == null) {
      return;
    }

    Room room = this.world.getCurrentRoom();

    int tileWidth = g.getClipBounds().width / (int)room.getXSize();
    int tileHeight = g.getClipBounds().height / (int)room.getYSize();

    for (Tile tile : room.getTiles()) {
      if (tile.isBlocking()) {
        g.setColor(this.blockingTile);
      } else {
        g.setColor(this.passableTile);
      }

      g.fillRect((int)(tile.getPosition().getX() * tileWidth), (int)(tile.getPosition().getY() * tileHeight), tileWidth, tileHeight);
    }

    for (Enemy enemy : room.getEnemies()) {
      Position position = enemy.getPosition();

      g.setColor(this.enemyColor);
      g.fillRect((int)(position.getX() * tileWidth), (int)(position.getY() * tileHeight), tileWidth, tileHeight);
    }

    Position playerPosition = this.world.getPlayer().getPosition();

    g.setColor(this.playerColor);
    g.fillRect((int)(playerPosition.getX() * tileWidth), (int)(playerPosition.getY() * tileHeight), tileWidth, tileHeight);
  }
}
