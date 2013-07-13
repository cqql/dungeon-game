package dungeon.ui.messages;

import dungeon.messages.Message;

import java.io.Serializable;

public class ChatMessage implements Message, Serializable {
  private final int authorId;

  private final String text;

  public ChatMessage (int authorId, String text) {
    this.authorId = authorId;
    this.text = text;
  }

  public int getAuthorId () {
    return authorId;
  }

  public String getText () {
    return text;
  }
}
