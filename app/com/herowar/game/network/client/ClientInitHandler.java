package com.herowar.game.network.client;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.dao.game.MatchTokenDAO;
import com.herowar.game.EventKeys;
import com.herowar.game.network.Connection;
import com.herowar.game.network.server.AccessDeniedPacket;
import com.herowar.game.network.server.AccessGrantedPacket;
import com.herowar.models.entity.game.MatchToken;
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
