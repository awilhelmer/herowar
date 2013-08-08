package com.herowar.game.network.client;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.game.network.Connection;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

public class ClientTutorialUpdateHandler {
	private static final Logger.ALogger log = Logger.of(ClientTutorialUpdateHandler.class);

	@Observer(topic = "ClientTutorialUpdatePacket", referenceStrength = ReferenceStrength.STRONG)
	public static void observe(final Connection connection, final ClientTutorialUpdatePacket packet) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				log.info("Request next tutorial step...");
				connection.game().setTutorialUpdate(true);
			}
		});
	}

}
