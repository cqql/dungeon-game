package dungeon.ui;

import dungeon.LevelLoadHandler;
import dungeon.events.Event;
import dungeon.events.EventHandler;
import dungeon.models.*;
import dungeon.models.events.TransformEvent;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Canvas extends JPanel implements EventHandler {
  private final Color blockingTile = new Color(181, 125, 147);

  private final Color passableTile = new Color(139, 108, 217);

  private final Color playerColor = new Color(101, 202, 227);

  private final Color enemyColor = new Color(33, 237, 60);

  private World world;

  @Override
  public void handleEvent (Event event) {
    if (event instanceof TransformEvent) {
      this.world = this.world.apply(((TransformEvent)event).getTransform());
    } else if (event instanceof LevelLoadHandler.LevelLoadedEvent) {
      this.world = ((LevelLoadHandler.LevelLoadedEvent)event).getWorld();
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

    int tileWidth = g.getClipBounds().width / room.getSize();
    int tileHeight = g.getClipBounds().height / room.getSize();
    int row = 0;

    for (List<Tile> tiles : room.getTiles()) {
      int column = 0;

      for (Tile tile : tiles) {
        if (tile.isBlocking()) {
          g.setColor(this.blockingTile);
        } else {
          g.setColor(this.passableTile);
        }

        g.fillRect(column * tileWidth, row * tileHeight, tileWidth, tileHeight);

        column++;
      }

      row++;
    }

    for (Enemy enemy : room.getEnemies()) {
      Position position = enemy.getPosition();

      g.setColor(this.enemyColor);
      g.fillRect((int)position.getX() * tileWidth, (int)position.getY() * tileHeight, tileWidth, tileHeight);
    }

    Position playerPosition = this.world.getPlayer().getPosition();

    g.setColor(this.playerColor);
    g.fillRect((int)playerPosition.getX() * tileWidth, (int)playerPosition.getY() * tileHeight, tileWidth, tileHeight);
  }
}
