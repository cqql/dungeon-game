package dungeon.ui;

import dungeon.ui.events.MoveCommand;
import dungeon.events.Event;
import dungeon.events.EventHandler;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel implements EventHandler {
  private static final int START_POSITION = 200;

  private Point point = new Point(START_POSITION, START_POSITION);

  @Override
  public void handleEvent (Event event) {
    if (event instanceof MoveCommand) {
      this.move((MoveCommand)event);
    }
  }

  private void move (MoveCommand event) {
    switch (event) {
      case UP:
        this.point.y--;
        break;
      case DOWN:
        this.point.y++;
        break;
      case LEFT:
        this.point.x--;
        break;
      case RIGHT:
        this.point.x++;
        break;
      default:
    }

    repaint();
  }

  @Override
  protected void paintComponent (Graphics g) {
    super.paintComponent(g);

    g.drawLine(0, 0, this.point.x, this.point.y);
  }
}
