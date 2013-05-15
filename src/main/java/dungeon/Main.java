package dungeon;

import dungeon.messages.EventHost;
import dungeon.ui.Canvas;
import dungeon.ui.InputToEventConverter;
import dungeon.ui.MainFrame;
import dungeon.ui.SwingConsumer;

public class Main {
  public static void main (String[] args) {
    Log.setLevel(Log.Level.NOTICE);

    EventHost eventHost = new EventHost();

    MainFrame mainFrame = new MainFrame(eventHost);

    InputToEventConverter converter = new InputToEventConverter(eventHost);
    mainFrame.addKeyListener(converter);

    Canvas canvas = new Canvas();
    mainFrame.add(canvas);

    eventHost.addConsumer(new SwingConsumer(mainFrame));
    eventHost.addConsumer(new SwingConsumer(canvas));

    eventHost.addHandler(new LevelLoadHandler(eventHost));
    eventHost.addHandler(new GameHandler(eventHost));

    eventHost.run();
  }
}
