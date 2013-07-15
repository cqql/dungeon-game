package dungeon.ui.screens;

import dungeon.models.Quest;

import javax.swing.*;
import java.awt.Component;
import java.util.List;

public class QuestList extends JList<Quest> {
  private final DefaultListModel<Quest> listModel = new DefaultListModel<>();

  public QuestList () {
    this.setModel(this.listModel);

    this.setCellRenderer(new ListCellRenderer<Quest>() {
      @Override
      public Component getListCellRendererComponent (JList<? extends Quest> jList, Quest quest, int index, boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component cell = renderer.getListCellRendererComponent(jList, quest, index, isSelected, cellHasFocus);

        ((JLabel)cell).setText(quest.getName());

        return cell;
      }
    });
  }

  public void setItems (List<Quest> quests) {
    this.listModel.clear();

    for (Quest quest : quests) {
      this.listModel.addElement(quest);
    }
  }
}
