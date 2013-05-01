package dungeon;

import dungeon.events.Event;
import dungeon.events.EventHost;
import dungeon.ui.MainFrame;
import dungeon.ui.SwingConsumer;

public class Main {
  public static enum TickEvent implements Event {
    TICK
  }

  public static void main (String[] args) {
    Log.setLevel(Log.Level.NOTICE);

    EventHost eventHost = new EventHost();

    eventHost.addConsumer(new SwingConsumer(new MainFrame(eventHost)));

    eventHost.run();
  }
}
