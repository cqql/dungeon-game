package dungeon.ui.sound;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Preloads sounds and plays them on demand.
 */
public class SoundManager {
  private final Map<String, Sound> sounds = new HashMap<>();

  private final Sound backgroundMusic;

  private Sound.Control backgroundMusicControl;

  public SoundManager () throws IOException, UnsupportedAudioFileException {
    this.backgroundMusic = new Sound("sounds/background.wav");
  }

  /**
   * Loads a sound file from the resources folder.
   */
  public void loadSound (String resource) throws IOException, UnsupportedAudioFileException {
    this.sounds.put(resource, new Sound(resource));
  }

  /**
   * Plays a previously loaded sound.
   */
  public void playSound (String resource) {
    Sound sound = this.sounds.get(resource);

    if (sound != null) {
      sound.play();
    }
  }

  public void startBackgroundMusic () {
    this.backgroundMusicControl = this.backgroundMusic.loop();
  }

  public void stopBackgroundMusic () {
    if (this.backgroundMusicControl != null) {
      this.backgroundMusicControl.stop();
    }
  }
}
