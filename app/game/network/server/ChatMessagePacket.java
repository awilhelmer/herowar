package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * New chat message retrieved.
 * 
 * @authorAlexander Wilhelmer
 */
public class ChatMessagePacket extends BasePacket {
  private static final long serialVersionUID = 758911699262440495L;
  
  private String message;
	private ChatType chatType;

	public ChatMessagePacket(ChatType chatType, String message) {
		this.type = PacketType.ChatMessagePacket;
		this.message = message;
		this.chatType = chatType;
	}

	public ChatType getChatType() {
		return chatType;
	}

	public void setChatType(ChatType chatType) {
		this.chatType = chatType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public enum ChatType {
		SYSTEM, PLAYER
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((chatType == null) ? 0 : chatType.hashCode());
		result = prime * result
				+ ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatMessagePacket other = (ChatMessagePacket) obj;
		if (chatType != other.chatType)
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChatMessagePacket [createdTime=" + createdTime + ", message="
				+ message + ", chatType=" + chatType + "]";
	}

}
