package game.network.client;

import game.network.Connection;
import game.network.server.ChatMessagePacket;
import game.network.server.ChatMessagePacket.Layout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import play.db.jpa.JPA;

import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

public class ClientChatMessageHandler {
	private static final DateFormat df = new SimpleDateFormat("hh:mm");

	@Observer(topic = "ClientChatMessagePacket", referenceStrength = ReferenceStrength.STRONG)
	public static void observe(final Connection connection, final ClientChatMessagePacket packet) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				connection.game().broadcast(
						new ChatMessagePacket(Layout.USER, "[" + df.format(new Date()) + "] " + connection.user().getUsername() + ": "
								+ packet.getMessage()));
			}
		});
	}

}
