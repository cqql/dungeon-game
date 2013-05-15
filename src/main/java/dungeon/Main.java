package dungeon;

import dungeon.messages.Mailman;
import dungeon.ui.Canvas;
import dungeon.ui.InputToEventConverter;
import dungeon.ui.MainFrame;
import dungeon.ui.SwingConsumer;

public class Main {
  public static void main (String[] args) {
    Log.setLevel(Log.Level.NOTICE);

    Mailman mailman = new Mailman();

    MainFrame mainFrame = new MainFrame(mailman);

    InputToEventConverter converter = new InputToEventConverter(mailman);
    mainFrame.addKeyListener(converter);

    Canvas canvas = new Canvas();
    mainFrame.add(canvas);

    mailman.addMailbox(new SwingConsumer(mainFrame));
    mailman.addMailbox(new SwingConsumer(canvas));

    mailman.addHandler(new LevelLoadHandler(mailman));
    mailman.addHandler(new GameHandler(mailman));

    mailman.run();
  }
}
