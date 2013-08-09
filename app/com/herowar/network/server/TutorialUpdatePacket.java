package com.herowar.network.server;

import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;

/**
 * The TutorialUpdatePacket will be send from server to client to update the tutorial state.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TutorialUpdatePacket extends BasePacket {

	protected String[] texts;

	public TutorialUpdatePacket(String[] texts) {
		super();
		this.type = PacketType.TutorialUpdatePacket;
		this.texts = texts;
	}

	public String[] getTexts() {
		return texts;
	}

	public void setTexts(String[] texts) {
		this.texts = texts;
	}
}
