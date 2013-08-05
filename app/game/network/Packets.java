package game.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;

import com.ssachtleben.play.plugin.base.ClassUtils;

/**
 * The PacketHandler handles every packet incoming from a web connection and executes the depending packet class.
 * 
 * @author Sebastian Sachtleben
 */
public class Packets {
	private static final Logger.ALogger log = Logger.of(Packets.class);

	private Map<Integer, Class<? extends BasePacket>> packetTypes = findPacketTypes();

	private Packets() {
	}

	private static Packets instance = new Packets();

	private static Packets instance() {
		return instance;
	}

	public static void handle(final WebSocketConnection connection, final String data) {
		BasePacket packetType = Json.fromJson(Json.parse(data), BasePacket.class);
		if (packetType == null || !instance().packetTypes().containsKey(packetType.getType())) {
			log.error(String.format("Failed to get packet class for type: %s", (packetType != null ? packetType.getType() : null)));
			return;
		}
		try {
			final InputPacket packet = (InputPacket) Json.fromJson(Json.parse(data), instance().packetTypes().get(packetType.getType()));
			if (packet != null) {
				log.debug(String.format("Retrieved (connection %d): %s", connection.httpRequest().id(), packet.toString()));
				JPA.withTransaction(new play.libs.F.Callback0() {
					@Override
					public void invoke() throws Throwable {
						packet.process(connection);
					}
				});
			}
		} catch (Exception e) {
			log.error(String.format("Failed to parse input packet (connection=%d): %s", connection.httpRequest().id(), data), e);
		}
	}

	/**
	 * @return the packetTypes
	 */
	public Map<Integer, Class<? extends BasePacket>> packetTypes() {
		return packetTypes;
	}

	/**
	 * @return the packetTypes
	 */
	private Map<Integer, Class<? extends BasePacket>> findPacketTypes() {
		log.debug("Search for client packet classes");
		Map<Integer, Class<? extends BasePacket>> packetTypes = new HashMap<Integer, Class<? extends BasePacket>>();
		Set<Class<?>> classes = (Set<Class<?>>) ClassUtils.findAnnotated(ClientPacket.class);
		Iterator<Class<?>> iter = classes.iterator();
		while (iter.hasNext()) {
			Class<?> clazz = iter.next();
			log.info(String.format("Found %s", clazz));
			try {
				ClientPacket clientPacket = clazz.getAnnotation(ClientPacket.class);
				packetTypes.put(clientPacket.type(), clazz.asSubclass(BasePacket.class));
				log.info(String.format("Added [type=%s, class=%s]", clientPacket.type(), packetTypes.get(clientPacket.type())));
			} catch (ClassCastException e) {
				log.error(String.format("Failed to cast %s to %s", clazz, BasePacket.class), e);
			}
		}
		return packetTypes;
	}

}
