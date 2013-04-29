package dungeon;

import dungeon.events.EventHost;

public class Main {
  public static void main (String[] args) {
    Log.setLevel(Log.Level.NOTICE);

    EventHost eventHost = new EventHost();

    eventHost.run();
  }
}
