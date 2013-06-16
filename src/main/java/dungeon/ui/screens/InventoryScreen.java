package dungeon.ui.screens;

import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.Item;
import dungeon.models.World;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.ShowGame;
import dungeon.ui.messages.ShowInventory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.List;

public class InventoryScreen extends JPanel implements MessageHandler {
  private final Mailman mailman;

  private World world;

  private final JList<Item> itemList;

  private final ItemPanel itemPanel;

  private final JPanel actionBar;

  private final JButton backButton;

  public InventoryScreen (Mailman mailman) {
    this.mailman = mailman;

    this.itemList = new JList<>();
    this.itemPanel = new ItemPanel();
    this.actionBar = new JPanel();
    this.backButton = new JButton("Zurück");
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world = this.world.apply((Transform)message);
    } else if (message instanceof LevelLoadedEvent) {
      this.world = ((LevelLoadedEvent)message).getWorld();
    } else if (message == LifecycleEvent.INITIALIZE) {
      this.initialize();
    } else if (message instanceof ShowInventory) {
      List<Item> items = this.world.getPlayer().getItems();

      this.itemList.setListData(items.toArray(new Item[items.size()]));
    }
  }

  private void initialize () {
    this.backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        InventoryScreen.this.mailman.send(new ShowGame());
      }
    });

    this.itemList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged (ListSelectionEvent listSelectionEvent) {
        Item item = InventoryScreen.this.itemList.getSelectedValue();

        InventoryScreen.this.itemPanel.setItem(item);
      }
    });

    this.itemList.setCellRenderer(new ListCellRenderer<Item>() {
      @Override
      public Component getListCellRendererComponent (JList<? extends Item> jList, Item item, int i, boolean b, boolean b2) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component cell = renderer.getListCellRendererComponent(jList, item, i, b, b2);

        ((JLabel)cell).setText(item.getType().getName());

        return cell;
      }
    });

    this.actionBar.add(this.backButton);

    this.setLayout(new GridLayout(1, 3, 10, 0));
    this.actionBar.setLayout(new BoxLayout(this.actionBar, BoxLayout.Y_AXIS));

    this.actionBar.setMinimumSize(new Dimension(200, 0));
    this.itemList.setMinimumSize(new Dimension(300, 0));

    this.add(this.itemList);
    this.add(this.itemPanel);
    this.add(this.actionBar);
  }
}
