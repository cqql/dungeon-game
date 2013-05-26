package dungeon;

import dungeon.game.LogicHandler;
import dungeon.load.LevelLoadHandler;
import dungeon.messages.Mailman;
import dungeon.ui.*;
import dungeon.ui.screens.*;

public class Main {
  public static void main (String[] args) {
    Log.setLevel(Log.Level.NOTICE);

    Mailman mailman = new Mailman();

    InputToMessageConverter converter = new InputToMessageConverter(mailman);

    Canvas canvas = new Canvas();
    canvas.addKeyListener(converter);

    StartMenu startMenu = new StartMenu(mailman);

    WinScreen winScreen = new WinScreen(mailman);

    DefeatScreen defeatScreen = new DefeatScreen(mailman);

    UiManager uiManager = new UiManager(canvas, startMenu, winScreen, defeatScreen);

    MainFrame mainFrame = new MainFrame(mailman, uiManager);

    mailman.addMailbox(new SwingMailbox(mainFrame));
    mailman.addMailbox(new SwingMailbox(canvas));
    mailman.addMailbox(new SwingMailbox(converter));
    mailman.addMailbox(new SwingMailbox(uiManager));

    mailman.addHandler(new LevelLoadHandler(mailman));
    mailman.addHandler(new LogicHandler(mailman));

    mailman.run();
  }
}
