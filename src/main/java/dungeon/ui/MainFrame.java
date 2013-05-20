package dungeon.ui;

import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;

import javax.swing.*;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main frame for the application.
 *
 * It handles showing the different screens of the game.
 */
public class MainFrame extends JFrame implements MessageHandler {
  public static final String TITLE = "DUNGEON GAME";

  private final Mailman mailman;

  private final Canvas canvas;

  private final StartMenu startMenu;

  public MainFrame (Mailman mailman, Canvas canvas) {
    this.mailman = mailman;
    this.canvas = canvas;

    this.startMenu = new StartMenu(new StartMenu.Listener() {
      @Override
      public void onStart () {
        MainFrame.this.remove(MainFrame.this.startMenu);
        MainFrame.this.add(MainFrame.this.canvas);

        MainFrame.this.revalidate();

        MainFrame.this.canvas.requestFocus();
      }

      @Override
      public void onQuit () {
        MainFrame.this.mailman.send(LifecycleEvent.SHUTDOWN);
      }
    });
  }

  @Override
  public void handleMessage (Message message) {
    if (message == LifecycleEvent.INITIALIZE) {
      this.initialize();
    } else if (message == LifecycleEvent.SHUTDOWN) {
      this.dispose();
    }
  }

  private void initialize () {
    this.setName(TITLE);
    this.setTitle(TITLE);

    this.setUndecorated(true);
    this.setExtendedState(Frame.MAXIMIZED_BOTH);
    this.setResizable(false);

    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing (WindowEvent e) {
        MainFrame.this.mailman.send(LifecycleEvent.SHUTDOWN);
      }
    });

    this.add(this.startMenu);

    this.setVisible(true);
  }
}
