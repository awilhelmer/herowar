package game.network.client;

import game.EventKeys;
import game.network.Connection;
import game.network.server.AccessDeniedPacket;
import game.network.server.AccessGrantedPacket;
import models.entity.game.MatchToken;
import play.Logger;
import play.db.jpa.JPA;

import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

import dao.game.MatchTokenDAO;

public class ClientInitHandler {
	private static final Logger.ALogger log = Logger.of(ClientInitHandler.class);

	@Observer(topic = "ClientInitPacket", referenceStrength = ReferenceStrength.STRONG)
	public static void observe(final Connection connection, final ClientInitPacket packet) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				MatchToken matchToken = MatchTokenDAO.getTokenById(packet.getToken());
				if (matchToken != null) {
					log.info(String.format("Accepted [connection=%s, matchToken=%s]", connection, matchToken));
					Events.instance().publish(EventKeys.PLAYER_JOIN, connection, matchToken);
					connection.send(new AccessGrantedPacket());
				} else {
					connection.send(new AccessDeniedPacket());
				}
			}
		});
	}

}
