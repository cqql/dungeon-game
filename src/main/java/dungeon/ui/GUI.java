package dungeon.ui;

import dungeon.client.Client;
import dungeon.messages.Mailman;
import dungeon.ui.screens.*;

/**
 * A swing GUI to play the game.
 */
public class GUI {
  private final Mailman mailman = new Mailman();

  private final Client client = new Client(this.mailman);

  private final Canvas canvas = new Canvas(this.client);

  private final StartMenu startMenu = new StartMenu(this.client);

  private final WinScreen winScreen = new WinScreen(this.client);

  private final DefeatScreen defeatScreen = new DefeatScreen(this.client);

  private final InventoryScreen inventoryScreen = new InventoryScreen(this.client);

  private final ShopScreen shopScreen = new ShopScreen(this.client);

  private final InputToMessageConverter converter = new InputToMessageConverter(this.client);

  private final UiManager uiManager = new UiManager(this.client, this.canvas, this.startMenu, this.winScreen, this.defeatScreen, this.inventoryScreen, this.shopScreen);

  private final MainFrame mainFrame = new MainFrame(this.client, this.uiManager);

  public GUI () {
    this.canvas.addKeyListener(this.converter);

    this.mailman.addMailbox(new SwingMailbox(this.client));
    this.mailman.addMailbox(new SwingMailbox(this.mainFrame));
    this.mailman.addMailbox(new SwingMailbox(this.canvas));
    this.mailman.addMailbox(new SwingMailbox(this.uiManager));
    this.mailman.addMailbox(new SwingMailbox(this.inventoryScreen));
    this.mailman.addMailbox(new SwingMailbox(this.shopScreen));
  }

  public void run () {
    this.mailman.run();
  }
}
