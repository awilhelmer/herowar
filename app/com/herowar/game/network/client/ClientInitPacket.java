package com.herowar.game.network.client;

import com.herowar.game.network.ClientPacket;
import com.herowar.game.network.PacketType;

/**
 * Initial client packet contains game token.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientInitPacket)
@SuppressWarnings("serial")
public class ClientInitPacket extends BaseClientPacket {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientInitPacket other = (ClientInitPacket) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientInitPacket [type=" + type + ", createdTime=" + createdTime + ", token=" + token + "]";
	}
}
