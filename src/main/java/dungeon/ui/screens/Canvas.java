package dungeon.ui.screens;

import dungeon.client.Client;
import dungeon.game.messages.TalkToNpc;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.*;
import dungeon.ui.messages.ChatMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

public class Canvas extends JPanel implements MessageHandler {
  private static final Logger LOGGER = Logger.getLogger(Canvas.class.getName());

  /**
   * How long to show dialogs in milliseconds.
   */
  private static final int DIALOG_TIME = 3000;

  private final Color blockingTile = new Color(181, 125, 147);

  private final Color passableTile = new Color(139, 108, 217);

  private final Color victoryTile = new Color(255, 244, 25);

  private final Color teleporterTile = new Color(0, 0, 0);

  private final Color playerColor = new Color(101, 202, 227);

  private final Color npcColor = new Color(28, 31, 255);

  private final Color merchantColor = new Color(255, 124, 235);

  private final Color enemyColor = new Color(33, 237, 60);

  private final Color savePointColor = new Color(50, 122, 88);

  private final Color hpColor = new Color(235, 58, 58);

  private final Color lifeColor = new Color(60, 179, 113);

  private final Color manaColor = new Color(0, 160, 255);

  private final Color white = new Color(255, 255, 255);

  private final Color moneyColor = new Color(253, 225, 54);

  private final Color itemColor = new Color(151, 151, 151);

  private final Color healthPotionColor = new Color(200, 0, 0);

  private final Color manaPotionColor = new Color(0, 170, 255);

  private final Color projectileColor = new Color(118, 77, 0);

  private final Font font = new Font("Arial", Font.PLAIN, 20);

  private final Font infoFont = new Font("Arial", Font.PLAIN, 12);

  private final Image rock;

  private final Image paper;

  private final Image scissors;

  private final Deque<ChatMessage> chatMessages = new ArrayDeque<>();

  private final Client client;

  private long dialogTimeout;

  private NPC dialogNpc;

  private long questTimeout;

  private Quest quest;

  private long questSolvedTimeout;

  private Quest solvedQuest;

  /**
   * The unit to pixel conversion factors for the current room.
   */
  private double xPixelPerUnit;

  private double yPixelPerUnit;

  public Canvas (Client client) throws IOException {
    this.client = client;

    this.rock = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("icons/rock.png"));
    this.paper = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("icons/paper.png"));
    this.scissors = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("icons/scissors.png"));

    this.setFocusable(true);
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof TalkToNpc && ((TalkToNpc)message).getPlayerId() == this.client.getPlayerId()) {
      this.dialogTimeout = System.currentTimeMillis() + DIALOG_TIME;
      this.dialogNpc = ((TalkToNpc)message).getNpc();
    } else if (message instanceof ChatMessage) {
      this.chatMessages.addFirst((ChatMessage)message);
    } else if (message instanceof Player.AddQuestTransform && ((Player.AddQuestTransform)message).getPlayerId() == this.client.getPlayerId()) {
      this.quest = ((Player.AddQuestTransform)message).getQuest();
      this.questTimeout = System.currentTimeMillis() + DIALOG_TIME;
    } else if (message instanceof Player.SolveQuestTransform && ((Player.SolveQuestTransform)message).getPlayerId() == this.client.getPlayerId()) {
      this.solvedQuest = ((Player.SolveQuestTransform)message).getQuest();
      this.questSolvedTimeout = System.currentTimeMillis() + DIALOG_TIME;
    }

    repaint();
  }

  @Override
  protected void paintComponent (Graphics g) {
    super.paintComponent(g);

    Player player = this.client.getPlayer();
    Room room = this.client.getCurrentRoom();

    if (player == null || room == null) {
      return;
    }

    this.xPixelPerUnit = (double)g.getClipBounds().width / room.getXSize();
    this.yPixelPerUnit = (double)g.getClipBounds().height / room.getYSize();

    this.drawTiles(g, room);
    this.drawDrops(g, room);
    this.drawNPCs(g, room);
    this.drawMerchants(g, room);
    this.drawEnemies(g, room);
    this.drawSavepoints(g, room);

    for (Player p : this.client.getPlayersInRoom(player)) {
      this.drawPlayer(g, p);
    }

    this.drawProjectiles(g, room);
    this.drawHpIndicator(g, player);
    this.drawMoneyIndicator(g, player);
    this.drawLifeIndicator(g, player);
    this.drawManaIndicator(g, player);
    this.drawWeaponIndicator(g, player);
    this.drawChat(g);
    this.drawDialog(g);
    this.drawQuestAcquisition(g);
    this.drawQuestSolved(g);
  }

  private void drawTiles (Graphics g, Room room) {
    for (Tile tile : room.getTiles()) {
      if (tile instanceof TeleporterTile) {
        g.setColor(this.teleporterTile);
      } else if (tile instanceof VictoryTile) {
        g.setColor(this.victoryTile);
      } else if (tile.isBlocking()) {
        g.setColor(this.blockingTile);
      } else {
        g.setColor(this.passableTile);
      }

      this.drawSquare(g, tile.getPosition(), Tile.SIZE);
    }
  }

  private void drawDrops (Graphics g, Room room) {
    for (Drop drop : room.getDrops()) {
      if (drop.isMoney()) {
        g.setColor(this.moneyColor);
      } else if (drop.getItem().getType() == ItemType.HEALTH_POTION) {
        g.setColor(this.healthPotionColor);
      } else if (drop.getItem().getType() == ItemType.MANA_POTION) {
        g.setColor(this.manaPotionColor);
      }

      this.drawSquare(g, drop.getPosition(), Drop.SIZE);
    }
  }

  private void drawNPCs (Graphics g, Room room) {
    for (NPC npc : room.getNpcs()) {
      g.setColor(this.npcColor);

      this.drawSquare(g, npc.getPosition(), NPC.SIZE);
    }
  }

  private void drawMerchants (Graphics g, Room room) {
    for (Merchant merchant : room.getMerchants()) {
      g.setColor(this.merchantColor);

      this.drawSquare(g, merchant.getPosition(), Merchant.SIZE);
    }
  }

  private void drawEnemies (Graphics g, Room room) {
    for (Enemy enemy : room.getEnemies()) {
      g.setColor(this.enemyColor);

      this.drawSquare(g, enemy.getPosition(), Enemy.SIZE);

      g.setColor(Color.BLACK);
      g.setFont(this.infoFont);
      g.drawString(String.format("%d HP", enemy.getHitPoints()), (int)(enemy.getPosition().getX() * this.xPixelPerUnit) + 10, (int)(enemy.getPosition().getY() * this.yPixelPerUnit) + 22);

      Image icon = this.getTypeIcon(enemy.getType());
      Position enemyCenter = enemy.getCenter();
      g.drawImage(icon, (int)(enemyCenter.getX() * this.xPixelPerUnit) - 5, (int)(enemyCenter.getY() * this.yPixelPerUnit) - 5, null);
    }
  }

  private void drawSavepoints (Graphics g, Room room) {
    for (SavePoint savePoint : room.getSavePoints()) {
      g.setColor(this.savePointColor);

      this.drawSquare(g, savePoint.getPosition(), savePoint.SIZE);
    }
  }

  private void drawPlayer (Graphics g, Player player) {
    g.setColor(this.playerColor);

    this.drawSquare(g, player.getPosition(), Player.SIZE);
  }

  private void drawProjectiles (Graphics g, Room room) {
    for (Projectile projectile : room.getProjectiles()) {
      if (projectile.getType() == DamageType.NORMAL) {
        g.setColor(this.projectileColor);
        this.drawSquare(g, projectile.getPosition(), Projectile.SIZE);
      } else {
        g.drawImage(this.getTypeIcon(projectile.getType()), (int)(projectile.getPosition().getX() * this.xPixelPerUnit), (int)(projectile.getPosition().getY() * this.yPixelPerUnit), null);
      }
    }
  }

  private void drawHpIndicator (Graphics g, Player player) {
    g.setColor(this.hpColor);
    g.fillRect(20, 20, 20, 20);

    g.setColor(this.white);
    g.setFont(this.font);
    g.drawString(String.format("%d / %d", player.getHitPoints(), player.getMaxHitPoints()), 60, 38);
  }

  private void drawLifeIndicator (Graphics g, Player player) {
    g.setColor(this.lifeColor);
    g.fillRect(20, 100, 20, 20);

    g.setColor(this.white);
    g.setFont(this.font);
    g.drawString(String.format("%d", player.getLives()), 60, 118);
  }

  private void drawMoneyIndicator (Graphics g, Player player) {
    g.setColor(this.moneyColor);
    g.fillRect(20, 60, 20, 20);

    g.setColor(this.white);
    g.setFont(this.font);
    g.drawString(String.format("%d", player.getMoney()), 60, 78);
  }

  private void drawManaIndicator (Graphics g, Player player) {
    g.setColor(this.manaColor);
    g.fillRect(20, 140, 20, 20);

    g.setColor(this.white);
    g.setFont(this.font);
    g.drawString(String.format("%d / %d", player.getMana(), player.getMaxMana()), 60, 158);
  }

  private void drawWeaponIndicator (Graphics g, Player player) {
    Item weapon = player.getWeapon();
    String weaponName = "Keine Waffe";

    if (weapon != null) {
      weaponName = weapon.getType().getName();
    }

    g.setColor(this.white);
    g.setFont(this.font);
    g.drawString(weaponName, 120, 38);
  }

  /**
   * Draws the last 5 chat messages.
   */
  private void drawChat (Graphics g) {
    int screenHeight = g.getClipBounds().height;
    int i = 0;

    for (ChatMessage message : this.chatMessages) {
      if (i == 5) {
        break;
      }

      String line = String.format("%s: %s", message.getAuthor(), message.getText());

      g.setFont(this.font);
      g.drawString(line, 20, screenHeight - 20 - i * 35);

      i++;
    }
  }

  private void drawDialog (Graphics g) {
    if (System.currentTimeMillis() < this.dialogTimeout) {
      Rectangle bounds = g.getClipBounds();

      g.setColor(Color.BLACK);
      g.fillRect(10, 10, bounds.width - 20, 200);

      g.setFont(this.font);

      g.setColor(Color.YELLOW);
      g.drawString(this.dialogNpc.getName(), 20, 40);

      g.setColor(Color.WHITE);
      g.drawString(this.dialogNpc.getSaying(), 20, 80);
    }
  }

  private void drawQuestAcquisition (Graphics g) {
    if (System.currentTimeMillis() < this.questTimeout) {
      Rectangle bounds = g.getClipBounds();

      g.setColor(Color.BLACK);
      g.fillRect(10, bounds.height - 150, bounds.width - 20, 140);

      g.setFont(this.font);

      g.setColor(Color.YELLOW);
      g.drawString(String.format("Neuer Quest: %s", this.quest.getName()), 20, bounds.height - 120);

      g.setColor(Color.WHITE);
      g.drawString(this.quest.getText(), 20, bounds.height - 80);
    }
  }

  private void drawQuestSolved (Graphics g) {
    if (System.currentTimeMillis() < this.questSolvedTimeout) {
      Rectangle bounds = g.getClipBounds();

      g.setColor(Color.BLACK);
      g.fillRect(10, bounds.height - 150, bounds.width - 20, 140);

      g.setFont(this.font);

      g.setColor(Color.YELLOW);
      g.drawString(String.format("Quest gelöst: %s", this.solvedQuest.getName()), 20, bounds.height - 120);

      g.setColor(Color.WHITE);
      g.drawString(this.solvedQuest.getText(), 20, bounds.height - 80);
    }
  }

  /**
   * Draw a square with the positions converted from our abstract unit to pixels.
   *
   * The squares bounds are rounded up to prevent gaps between squares.
   */
  private void drawSquare (Graphics g, Position position, int widthUnits) {
    g.fillRect(
      (int)Math.ceil(position.getX() * this.xPixelPerUnit),
      (int)Math.ceil(position.getY() * this.yPixelPerUnit),
      (int)Math.ceil(widthUnits * this.xPixelPerUnit),
      (int)Math.ceil(widthUnits * this.yPixelPerUnit)
    );
  }

  private Image getTypeIcon (DamageType type) {
    switch (type) {
      case ROCK:
        return this.rock;
      case PAPER:
        return this.paper;
      case SCISSORS:
      default:
        return this.scissors;
    }
  }
}
