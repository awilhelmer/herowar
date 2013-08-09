package com.herowar.network.client;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.network.Connection;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

public class ClientWaveRequestHandler {
	private static final Logger.ALogger log = Logger.of(ClientWaveRequestHandler.class);

	@Observer(topic = "ClientWaveRequestPacket", referenceStrength = ReferenceStrength.STRONG)
	public static void observe(final Connection connection, final ClientWaveRequestPacket packet) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				log.info("Request next wave...");
				connection.game().setWaveRequest(true);
			}
		});
	}

}
