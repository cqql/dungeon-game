package dungeon;

import dungeon.game.LogicHandler;
import dungeon.load.LevelLoadHandler;
import dungeon.messages.Mailman;
import dungeon.ui.Canvas;
import dungeon.ui.InputToMessageConverter;
import dungeon.ui.MainFrame;
import dungeon.ui.SwingMailbox;

public class Main {
  public static void main (String[] args) {
    Log.setLevel(Log.Level.NOTICE);

    Mailman mailman = new Mailman();

    InputToMessageConverter converter = new InputToMessageConverter(mailman);

    Canvas canvas = new Canvas();
    canvas.addKeyListener(converter);

    MainFrame mainFrame = new MainFrame(mailman, canvas);

    mailman.addMailbox(new SwingMailbox(mainFrame));
    mailman.addMailbox(new SwingMailbox(canvas));
    mailman.addMailbox(new SwingMailbox(converter));

    mailman.addHandler(new LevelLoadHandler(mailman));
    mailman.addHandler(new LogicHandler(mailman));

    mailman.run();
  }
}
