package game.network;

import game.network.client.ClientChatMessagePacket;
import game.network.client.ClientInitPacket;
import game.network.client.ClientPreloadUpdatePacket;
import game.network.client.ClientTowerRequestPacket;
import game.network.client.ClientTutorialUpdatePacket;
import game.network.client.ClientWaveRequestPacket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.libs.Json;

import com.ssachtleben.play.plugin.base.ClassUtils;
import com.ssachtleben.play.plugin.event.EventResult;
import com.ssachtleben.play.plugin.event.Events;

/**
 * The PacketHandler handles every packet incoming from a web connection and executes the depending packet class.
 * 
 * @author Sebastian Sachtleben
 */
public class Packets {
	private static final Logger.ALogger log = Logger.of(Packets.class);

	private Map<Integer, Class<? extends BasePacket>> types = findTypes2();

	private Packets() {
	}

	private static Packets instance = new Packets();

	private static Packets instance() {
		return instance;
	}

	public static void handle(final Connection connection, final JsonNode data) {
		log.info(String.format("Received [connection=%s, data=%s]", connection, data.toString()));
		int type = data.get("type").getIntValue();
		if (!instance().types().containsKey(type)) {
			log.error(String.format("Failed to get packet class for type: %s", type));
			return;
		}
		try {
			final InputPacket packet = (InputPacket) Json.fromJson(data, instance().types().get(type));
			if (packet != null) {
				log.info(String.format("Found %s", packet.toString()));
				String eventName = packet.getClass().getSimpleName();
				EventResult result = Events.instance().publish(eventName, connection, packet);
				if (result == null || !result.isPublished()) {
					throw new RuntimeException(String.format("Found no subscriber for '%s' event", eventName));
				}
			}
		} catch (Exception e) {
			log.error(String.format("Failed to parse packet: %s", data), e);
		}
	}

	/**
	 * @return the packetTypes
	 */
	public Map<Integer, Class<? extends BasePacket>> types() {
		return types;
	}

	private Map<Integer, Class<? extends BasePacket>> findTypes2() {
		Map<Integer, Class<? extends BasePacket>> types = new HashMap<Integer, Class<? extends BasePacket>>();
		types.put(1, ClientInitPacket.class);
		types.put(10, ClientPreloadUpdatePacket.class);
		types.put(71, ClientChatMessagePacket.class);
		types.put(80, ClientTowerRequestPacket.class);
		types.put(86, ClientTutorialUpdatePacket.class);
		types.put(95, ClientWaveRequestPacket.class);
		return types;
	}

	/**
	 * Finds all packet classes in classpath.
	 * <p>
	 * TODO: This search should happend async during application start and not on the first handle request... TODO: Currently this method
	 * finds no classes, check why?!?
	 * </p>
	 * 
	 * @return the packetTypes
	 */
	private Map<Integer, Class<? extends BasePacket>> findTypes() {
		log.debug("Search for client packet classes");
		Map<Integer, Class<? extends BasePacket>> packetTypes = new HashMap<Integer, Class<? extends BasePacket>>();
		Set<Class<?>> classes = ClassUtils.findAnnotated(ClientPacket.class);
		log.info(String.format("Found %d", classes.size()));
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
