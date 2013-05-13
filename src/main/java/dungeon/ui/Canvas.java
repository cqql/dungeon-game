package dungeon.ui;

import dungeon.LevelLoadHandler;
import dungeon.events.Event;
import dungeon.events.EventHandler;
import dungeon.models.Position;
import dungeon.models.Room;
import dungeon.models.Tile;
import dungeon.models.World;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Canvas extends JPanel implements EventHandler {
  private final Color blockingTile = new Color(181, 125, 147);

  private final Color passableTile = new Color(139, 108, 217);

  private final Color playerColor = new Color(101, 202, 227);

  private World world;

  @Override
  public void handleEvent (Event event) {
    if (event instanceof LevelLoadHandler.LevelLoadedEvent) {
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

    Position playerPosition = this.world.getPlayer().getPosition();

    g.setColor(this.playerColor);
    g.fillRect((int)playerPosition.getX() * tileWidth, (int)playerPosition.getY() * tileHeight, tileWidth, tileHeight);
  }
}
