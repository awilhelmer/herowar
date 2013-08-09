package com.herowar.network.client;


/**
 * Provides authorization safty for client packet.
 * <p>
 * If the connection is not authorized an {@link RuntimeException} occur and the packet processing stops.
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public abstract class BaseClientAuthPacket extends BaseClientPacket {

}
