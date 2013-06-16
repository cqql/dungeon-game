package dungeon.ui.screens;

import dungeon.models.Item;

import javax.swing.*;
import java.awt.GridLayout;

/**
 * Shows information about an item.
 */
public class ItemPanel extends JPanel {
  private LabeledInfo nameInfo = new LabeledInfo("Name");

  private LabeledInfo description = new LabeledInfo("Beschreibung");

  public ItemPanel () {
    this.setLayout(new GridLayout(25, 1));

    this.add(this.nameInfo);
    this.add(this.description);
  }

  public void setItem (Item item) {
    if (item == null) {
      this.nameInfo.setInfo("");
      this.description.setInfo("");
    } else {
      this.nameInfo.setInfo(item.getType().getName());
      this.description.setInfo(item.getType().getDescription());
    }
  }

  private static class LabeledInfo extends JPanel {
    private final JLabel label;

    private final JLabel info;

    public LabeledInfo (String label) {
      this.setLayout(new GridLayout(1, 2));

      this.label = new JLabel(label);
      this.info = new JLabel("");

      this.add(this.label);
      this.add(this.info);
    }

    public void setInfo (String info) {
      this.info.setText(info);
    }
  }
}
