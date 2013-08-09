package com.herowar.network;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import play.libs.Json;

/**
 * Used to detect packet type.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class BasePacket implements Serializable {

	protected int type;
	protected long createdTime;

	/**
	 * Default constructor for parsing incoming packets to base packet.
	 */
	public BasePacket() {
		this.createdTime = (new Date()).getTime();
	}

	/**
	 * Additional constructor to pass packet type.
	 * 
	 * @param type
	 *          The packet type.
	 */
	public BasePacket(int type) {
		this();
		this.type = type;
	}

	/**
	 * @return This packet as {@link JsonNode}.
	 */
	public JsonNode toJson() {
		return Json.toJson(this);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (createdTime ^ (createdTime >>> 32));
		result = prime * result + type;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasePacket other = (BasePacket) obj;
		if (createdTime != other.createdTime)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BasePacket [type=" + type + ", createdTime=" + createdTime + "]";
	}
}
