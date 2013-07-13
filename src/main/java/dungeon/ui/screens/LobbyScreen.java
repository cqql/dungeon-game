package dungeon.ui.screens;

import dungeon.client.Client;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;

import javax.swing.*;
import java.awt.GridLayout;

public class LobbyScreen extends JPanel implements MessageHandler {
  private final Client client;

  public LobbyScreen (Client client) {
    super(new GridLayout(5, 1));

    this.client = client;
  }

  @Override
  public void handleMessage (Message message) {

  }
}
