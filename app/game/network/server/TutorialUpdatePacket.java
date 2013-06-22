package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * The TutorialUpdatePacket will be send from server to client to update the
 * tutorial state.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TutorialUpdatePacket extends BasePacket {

  protected long state;
  protected String[] texts;

  public TutorialUpdatePacket(long state, String[] texts) {
    super();
    this.type = PacketType.TutorialUpdatePacket;
    this.state = state;
    this.texts = texts;
  }

  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }

  public String[] getTexts() {
    return texts;
  }

  public void setTexts(String[] texts) {
    this.texts = texts;
  }
}
