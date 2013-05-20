package dungeon.ui;

import dungeon.game.messages.DefeatEvent;
import dungeon.game.messages.WinEvent;
import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;

import javax.swing.*;
import java.awt.CardLayout;
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

  private static final String START_MENU = "START_MENU";
  private static final String CANVAS = "CANVAS";
  private static final String WIN_SCREEN = "WIN_SCREEN";
  private static final String DEFEAT_SCREEN = "DEFEAT_SCREEN";

  private final Mailman mailman;

  private final Canvas canvas;

  private final StartMenu startMenu;

  private final WinScreen winScreen;

  private final DefeatScreen defeatScreen;

  private final JPanel screenManager;

  public MainFrame (Mailman mailman, Canvas canvas) {
    this.mailman = mailman;
    this.canvas = canvas;

    this.screenManager = new JPanel(new CardLayout());

    this.startMenu = new StartMenu(new StartMenu.Listener() {
      @Override
      public void onStart () {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run () {
            MainFrame.this.goToScreen(CANVAS);

            MainFrame.this.canvas.requestFocus();
          }
        });
      }

      @Override
      public void onQuit () {
        MainFrame.this.mailman.send(LifecycleEvent.SHUTDOWN);
      }
    });

    this.winScreen = new WinScreen(new WinScreen.Listener() {
      @Override
      public void onBackButton () {
        MainFrame.this.goToScreen(START_MENU);
      }
    });

    this.defeatScreen = new DefeatScreen(new DefeatScreen.Listener() {
      @Override
      public void onBackButton () {
        MainFrame.this.goToScreen(START_MENU);
      }
    });

    this.screenManager.add(this.startMenu, START_MENU);
    this.screenManager.add(this.canvas, CANVAS);
    this.screenManager.add(this.winScreen, WIN_SCREEN);
    this.screenManager.add(this.defeatScreen, DEFEAT_SCREEN);

    this.add(this.screenManager);
  }

  @Override
  public void handleMessage (Message message) {
    if (message == LifecycleEvent.INITIALIZE) {
      this.initialize();
    } else if (message == LifecycleEvent.SHUTDOWN) {
      this.dispose();
    } else if (message instanceof WinEvent) {
      this.goToScreen(WIN_SCREEN);
    } else if (message instanceof DefeatEvent) {
      this.goToScreen(DEFEAT_SCREEN);
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

    this.goToScreen(START_MENU);

    this.setVisible(true);
  }

  private void goToScreen (String index) {
    CardLayout layout = (CardLayout)this.screenManager.getLayout();

    layout.show(this.screenManager, index);

    this.revalidate();
  }
}
