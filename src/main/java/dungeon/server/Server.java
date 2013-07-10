package dungeon.server;

import dungeon.game.LogicHandler;
import dungeon.load.LevelLoadHandler;
import dungeon.messages.Mailman;
import dungeon.pulse.PulseGenerator;

public class Server {
  private final Mailman mailman = new Mailman();

  public Server () {
    this.mailman.addMailbox(new PulseGenerator(this.mailman));
    this.mailman.addHandler(new LevelLoadHandler(this.mailman));
    this.mailman.addHandler(new LogicHandler(this.mailman));
  }

  public Mailman getMailman () {
    return mailman;
  }

  public void run () {
    this.mailman.run();
  }
}
