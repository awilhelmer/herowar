package com.herowar.network.server;

import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;

/**
 * The GUIElementUpdatePacket will be send from server to client to update the gui.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GUIElementUpdatePacket extends BasePacket {

	protected String name;
	protected boolean visible;
	protected boolean active;

	public GUIElementUpdatePacket(String name, boolean visible, boolean active) {
		super();
		this.type = PacketType.GUIElementUpdatePacket;
		this.name = name;
		this.visible = visible;
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}