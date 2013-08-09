package com.herowar.network;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.ardor3d.math.type.ReadOnlyVector3;

/**
 * Abstract ObjectPacket class for several object packets.
 * 
 * @author Sebastian Sachtleben
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public abstract class ObjectPacket extends BasePacket {

	protected long id;
	protected ReadOnlyVector3 position;

	public ObjectPacket(long id, ReadOnlyVector3 position) {
		super();
		this.id = id;
		this.position = position;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ReadOnlyVector3 getPosition() {
		return position;
	}

	public void setPosition(ReadOnlyVector3 position) {
		this.position = position;
	}
}
