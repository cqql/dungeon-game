package dungeon.ui.sound;

import dungeon.game.messages.DefeatEvent;
import dungeon.game.messages.StartGame;
import dungeon.game.messages.WinEvent;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
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
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof StartCommand && ((StartCommand)message).getCommand() instanceof AttackCommand) {
      this.playShotSound();
    } else if (message instanceof StartGame) {
      this.soundManager.startBackgroundMusic();
    } else if (message instanceof WinEvent || message instanceof DefeatEvent) {
      this.soundManager.stopBackgroundMusic();
    }
  }

  private void playShotSound () {
    this.soundManager.playSound("sounds/shot.wav");
  }
}
