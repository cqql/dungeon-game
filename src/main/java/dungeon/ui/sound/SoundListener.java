package dungeon.ui.sound;

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
    }
  }

  private void playShotSound () {
    this.soundManager.playSound("sounds/shot.wav");
  }
}
