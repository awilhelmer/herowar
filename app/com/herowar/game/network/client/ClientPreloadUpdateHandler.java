package com.herowar.game.network.client;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.game.network.Connection;
import com.herowar.game.network.server.GameStartPacket;
import com.herowar.game.processor.GameProcessor;
import com.herowar.game.processor.GameProcessor.Topic;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

public class ClientPreloadUpdateHandler {
	private static final Logger.ALogger log = Logger.of(ClientPreloadUpdateHandler.class);

	@Observer(topic = "ClientPreloadUpdatePacket", referenceStrength = ReferenceStrength.STRONG)
	public static void observe(final Connection connection, final ClientPreloadUpdatePacket packet) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				if (GameProcessor.State.PRELOAD.equals(connection.game().getState())) {
					if (packet.getProgress() == 100) {
						log.info("Send preload complete event to " + connection.game().getTopicName() + " for " + connection.user().getUsername());
						connection.preloading(false);
					}
					connection.game().publish(Topic.PRELOAD, connection, packet.getProgress());
				} else if (packet.getProgress() == 100) {
					connection.send(new GameStartPacket());
					connection.game().syncronize(connection);
					connection.preloading(false);
				}
			}
		});
	}

}
