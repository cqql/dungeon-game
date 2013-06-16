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
import dungeon.ui.messages.UseItemCommand;

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

  private final DefaultListModel<Item> listModel = new DefaultListModel<>();

  private final JList<Item> itemList = new JList<>(this.listModel);

  private final ItemPanel itemPanel = new ItemPanel();

  private final JPanel actionBar = new JPanel();

  private final JButton equipButton = new JButton("Ausrüsten");

  private final JButton useButton = new JButton("Benutzen");

  private final JButton backButton = new JButton("Zurück");

  public InventoryScreen (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world = this.world.apply((Transform)message);

      this.reset();
    } else if (message instanceof LevelLoadedEvent) {
      this.world = ((LevelLoadedEvent)message).getWorld();
    } else if (message == LifecycleEvent.INITIALIZE) {
      this.initialize();
    } else if (message instanceof ShowInventory) {
      this.reset();
    }
  }

  /**
   * Synchronize the items in the list with the items in the player's bag and enable/disable the buttons.
   */
  private void reset () {
    List<Item> items = this.world.getPlayer().getItems();

    for (Item item : items) {
      if (!this.listModel.contains(item)) {
        this.listModel.addElement(item);
      }
    }

    for (Object object : this.listModel.toArray()) {
      Item item = (Item)object;

      if (!items.contains(item)) {
        this.listModel.removeElement(item);
      }
    }

    if (this.itemList.getSelectedValue() == null) {
      this.equipButton.setEnabled(false);
      this.useButton.setEnabled(false);
    }
  }

  private void initialize () {
    this.equipButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (!InventoryScreen.this.equipButton.isEnabled()) {
          return;
        }

        Item item = InventoryScreen.this.itemList.getSelectedValue();
      }
    });

    this.useButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (!InventoryScreen.this.useButton.isEnabled()) {
          return;
        }

        Item item = InventoryScreen.this.itemList.getSelectedValue();

        InventoryScreen.this.mailman.send(new UseItemCommand(item));
      }
    });

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

        if (item == null) {
          InventoryScreen.this.reset();
        } else {
          InventoryScreen.this.equipButton.setEnabled(item.getType().isEquipable());
          InventoryScreen.this.useButton.setEnabled(item.getType().isUseable());
        }
      }
    });

    this.itemList.setCellRenderer(new ListCellRenderer<Item>() {
      @Override
      public Component getListCellRendererComponent (JList<? extends Item> jList, Item item, int index, boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component cell = renderer.getListCellRendererComponent(jList, item, index, isSelected, cellHasFocus);

        ((JLabel)cell).setText(item.getType().getName());

        return cell;
      }
    });

    this.actionBar.add(this.equipButton);
    this.actionBar.add(this.useButton);
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
