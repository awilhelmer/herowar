package game.network.server;

import game.network.ObjectPacket;

import com.ardor3d.math.type.ReadOnlyVector3;

/**
 * The ObjectInPacket will be send from server to client to tell a new object entered the map.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ObjectInPacket extends ObjectPacket {

	protected String name;

	public ObjectInPacket(long id, String name, ReadOnlyVector3 position) {
		super(id, position);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
