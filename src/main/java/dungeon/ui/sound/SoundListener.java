package dungeon.ui.sound;

import dungeon.client.ServerDisconnected;
import dungeon.game.messages.DefeatEvent;
import dungeon.game.messages.StartGame;
import dungeon.game.messages.WinEvent;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.Player;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.AttackCommand;
import dungeon.ui.messages.StartCommand;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Plays sounds on certain events.
 */
public class SoundListener implements MessageHandler {
  private final SoundManager soundManager = new SoundManager();

  public SoundListener () throws UnsupportedAudioFileException, IOException {
    this.soundManager.loadSound("sounds/shot.wav");
    this.soundManager.loadSound("sounds/teleportation.wav");
    this.soundManager.loadSound("sounds/coins.wav");
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof StartCommand && ((StartCommand)message).getCommand() instanceof AttackCommand) {
      this.soundManager.playSound("sounds/shot.wav");
    } else if (message instanceof Transform) {
      if (message instanceof Player.TeleportTransform) {
        this.soundManager.playSound("sounds/teleportation.wav");
      } else if (message instanceof Player.MoneyTransform) {
        this.soundManager.playSound("sounds/coins.wav");
      }
    } else if (message instanceof StartGame) {
      this.soundManager.startBackgroundMusic();
    } else if (message instanceof WinEvent || message instanceof DefeatEvent || message instanceof ServerDisconnected) {
      this.soundManager.stopBackgroundMusic();
    }
  }
}
