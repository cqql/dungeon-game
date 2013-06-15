package dungeon.ui.screens;

import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.*;
import dungeon.models.messages.Transform;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Canvas extends JPanel implements MessageHandler {
  private final Color blockingTile = new Color(181, 125, 147);

  private final Color passableTile = new Color(139, 108, 217);

  private final Color victoryTile = new Color(255, 244, 25);

  private final Color teleporterTile = new Color(0, 0, 0);

  private final Color playerColor = new Color(101, 202, 227);

  private final Color enemyColor = new Color(33, 237, 60);

  private final Color savePointColor = new Color(50, 122, 88);

  private final Color hpColor = new Color(235, 58, 58);

  private final Color white = new Color(255, 255, 255);

  private final Font font = new Font("Arial", Font.PLAIN, 20);

  private World world;

  public Canvas () {
    this.setFocusable(true);
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world = this.world.apply((Transform) message);
    } else if (message instanceof LevelLoadedEvent) {
      this.world = ((LevelLoadedEvent) message).getWorld();
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

    double xPixelPerUnit = (double)g.getClipBounds().width / room.getXSize();
    double yPixelPerUnit = (double)g.getClipBounds().height / room.getYSize();

    for (Tile tile : room.getTiles()) {
      if (tile instanceof TeleporterTile) {
        g.setColor(this.teleporterTile);
      } else if (tile instanceof VictoryTile) {
        g.setColor(this.victoryTile);
      } else if (tile.isBlocking()) {
        g.setColor(this.blockingTile);
      } else {
        g.setColor(this.passableTile);
      }

      g.fillRect((int)(tile.getPosition().getX() * xPixelPerUnit), (int)(tile.getPosition().getY() * yPixelPerUnit), (int)(Tile.SIZE * xPixelPerUnit), (int)(Tile.SIZE * yPixelPerUnit));
    }

    for (Enemy enemy : room.getEnemies()) {
      Position position = enemy.getPosition();

      g.setColor(this.enemyColor);
      g.fillRect((int)(position.getX() * xPixelPerUnit), (int)(position.getY() * yPixelPerUnit), (int)(Enemy.SIZE * xPixelPerUnit), (int)(Enemy.SIZE * yPixelPerUnit));
    }

    for (SavePoint savePoint : room.getSavePoints()) {
      Position position = savePoint.getPosition();

      g.setColor(this.savePointColor);
      g.fillRect((int)(position.getX() * xPixelPerUnit), (int)(position.getY() * yPixelPerUnit), (int)(savePoint.SIZE * xPixelPerUnit), (int)(savePoint.SIZE * yPixelPerUnit));
    }

    Position playerPosition = this.world.getPlayer().getPosition();

    g.setColor(this.playerColor);
    g.fillRect((int)(playerPosition.getX() * xPixelPerUnit), (int)(playerPosition.getY() * yPixelPerUnit), (int)(Player.SIZE * xPixelPerUnit), (int)(Player.SIZE * yPixelPerUnit));

    this.drawHpIndicator(g);
  }

  /**
   * Draw an hitpoint indicator.
   */
  private void drawHpIndicator (Graphics g) {
    g.setColor(this.hpColor);
    g.fillRect(20, 20, 20, 20);

    g.setColor(this.white);
    g.setFont(this.font);
    g.drawString(String.format("%d / %d", this.world.getPlayer().getHitPoints(), this.world.getPlayer().getMaxHitPoints()), 60, 38);
  }
}
