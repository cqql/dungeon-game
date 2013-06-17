package dungeon.ui.screens;

import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.Item;
import dungeon.models.Merchant;
import dungeon.models.World;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.BuyCommand;
import dungeon.ui.messages.SellCommand;
import dungeon.ui.messages.ShowGame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

public class ShopScreen extends JPanel implements MessageHandler {
  private final Mailman mailman;

  private World world;

  private Merchant merchant;

  private ItemList playerItemList = new ItemList();

  private ItemList merchantItemList = new ItemList();

  private ItemPanel itemPanel = new ItemPanel();

  private final JPanel actionBar = new JPanel();

  private final JButton buyButton = new JButton("Kaufen");

  private final JButton sellButton = new JButton("Verkaufen");

  private final JButton backButton = new JButton("Zurück");

  public ShopScreen (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world = this.world.apply((Transform)message);

      if (this.merchant != null) {
        this.merchant = this.merchant.apply((Transform)message);
      }

      if (message instanceof Merchant.InteractTransform) {
        this.merchant = ((Merchant.InteractTransform)message).getMerchant();
      }

      this.reset();
    } else if (message instanceof LevelLoadedEvent) {
      this.world = ((LevelLoadedEvent)message).getWorld();
    } else if (message == LifecycleEvent.INITIALIZE) {
      this.initialize();
    }
  }

  private void reset () {
    this.playerItemList.setItems(this.world.getPlayer().getItems());

    if (this.merchant != null) {
      this.merchantItemList.setItems(this.merchant.getItems());
    }
  }

  private void initialize () {
    this.setLayout(new GridLayout(1, 4));
    this.add(this.playerItemList);
    this.add(this.itemPanel);
    this.add(this.merchantItemList);
    this.add(this.actionBar);

    this.actionBar.setLayout(new BoxLayout(this.actionBar, BoxLayout.Y_AXIS));
    this.actionBar.add(this.buyButton);
    this.actionBar.add(this.sellButton);
    this.actionBar.add(this.backButton);

    this.backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        ShopScreen.this.mailman.send(new ShowGame());
      }
    });

    this.playerItemList.addListSelectionListener(itemPanelListener);
    this.merchantItemList.addListSelectionListener(itemPanelListener);

    this.buyButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        Item item = ShopScreen.this.merchantItemList.getSelectedValue();

        if (item == null) {
          return;
        }

        ShopScreen.this.mailman.send(new BuyCommand(ShopScreen.this.merchant, item));
      }
    });

    this.sellButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        Item item = ShopScreen.this.playerItemList.getSelectedValue();

        if (item == null) {
          return;
        }

        ShopScreen.this.mailman.send(new SellCommand(ShopScreen.this.merchant, item));
      }
    });
  }

  /**
   * Show the selected item in the item panel.
   */
  private ListSelectionListener itemPanelListener = new ListSelectionListener() {
    @Override
    public void valueChanged (ListSelectionEvent listSelectionEvent) {
      Item item = ((JList<Item>)listSelectionEvent.getSource()).getSelectedValue();

      ShopScreen.this.itemPanel.setItem(item);
    }
  };
}
