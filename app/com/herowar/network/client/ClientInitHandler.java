package com.herowar.network.client;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.EventKeys;
import com.herowar.dao.MatchTokenDAO;
import com.herowar.models.entity.game.MatchToken;
import com.herowar.network.Connection;
import com.herowar.network.server.AccessDeniedPacket;
import com.herowar.network.server.AccessGrantedPacket;
import com.ssachtleben.play.plugin.event.Events;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;


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
