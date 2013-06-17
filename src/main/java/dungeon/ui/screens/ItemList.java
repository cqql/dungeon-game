package dungeon.ui.screens;

import dungeon.models.Item;

import javax.swing.*;
import java.awt.Component;
import java.util.List;

public class ItemList extends JList<Item> {
  private final DefaultListModel<Item> listModel = new DefaultListModel<>();

  public ItemList () {
    this.setModel(listModel);

    this.setCellRenderer(new ListCellRenderer<Item>() {
      @Override
      public Component getListCellRendererComponent (JList<? extends Item> jList, Item item, int index, boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component cell = renderer.getListCellRendererComponent(jList, item, index, isSelected, cellHasFocus);

        ((JLabel)cell).setText(item.getType().getName());

        return cell;
      }
    });
  }

  public void setItems (List<Item> items) {
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
  }
}
