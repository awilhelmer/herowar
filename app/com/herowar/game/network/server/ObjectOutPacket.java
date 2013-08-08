package com.herowar.game.network.server;

import com.herowar.game.network.ObjectPacket;

/**
 * The ObjectOutPacket will be send from server to client to tell a object left the map.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ObjectOutPacket extends ObjectPacket {

	public ObjectOutPacket(long id) {
		super(id, null);
	}

	@Override
	public String toString() {
		return "ObjectOutPacket [type=" + type + ", id=" + id + ", position=" + position.toString() + "]";
	}
}
