package dungeon;

import dungeon.game.LogicHandler;
import dungeon.load.LevelLoadHandler;
import dungeon.messages.Mailman;
import dungeon.pulse.PulseGenerator;
import dungeon.ui.*;
import dungeon.ui.screens.*;

public class Main {
  public static void main (String[] args) {
    Mailman mailman = new Mailman();
    Client client = new Client(mailman);

    Canvas canvas = new Canvas(client);
    StartMenu startMenu = new StartMenu(client);
    WinScreen winScreen = new WinScreen(client);
    DefeatScreen defeatScreen = new DefeatScreen(client);
    InventoryScreen inventoryScreen = new InventoryScreen(client);
    ShopScreen shopScreen = new ShopScreen(client);

    InputToMessageConverter converter = new InputToMessageConverter(client);
    canvas.addKeyListener(converter);

    UiManager uiManager = new UiManager(canvas, startMenu, winScreen, defeatScreen, inventoryScreen, shopScreen);

    MainFrame mainFrame = new MainFrame(client, uiManager);

    mailman.addMailbox(new SwingMailbox(client));
    mailman.addMailbox(new SwingMailbox(mainFrame));
    mailman.addMailbox(new SwingMailbox(canvas));
    mailman.addMailbox(new SwingMailbox(uiManager));
    mailman.addMailbox(new SwingMailbox(inventoryScreen));
    mailman.addMailbox(new SwingMailbox(shopScreen));

    mailman.addMailbox(new PulseGenerator(mailman));

    mailman.addHandler(new LevelLoadHandler(mailman));
    mailman.addHandler(new LogicHandler(mailman));

    mailman.run();
  }
}
