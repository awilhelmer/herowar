package game.network;

/**
 * The PacketType class contains all packet type declarations from client and server. The value should be unique because the packet handler
 * needs something to identify the packet.
 * 
 * @author Sebastian Sachtleben
 */
public class PacketType {

	// Client
	public static final int ClientInitPacket = 1;
	public static final int ClientPreloadUpdatePacket = 10;
	public static final int ClientChatMessagePacket = 71;
	public static final int ClientTowerRequestPacket = 80;
	public static final int ClientTutorialUpdatePacket = 86;
	public static final int ClientWaveRequestPacket = 95;

	// Server
	public static final int AccessDeniedPacket = 11;
	public static final int AccessGrantedPacket = 12;
	public static final int GameStartPacket = 13;
	public static final int UnitInPacket = 16;
	public static final int UnitOutPacket = 17;
	public static final int PlayerLeavePacket = 21;
	public static final int PlayerInitPacket = 22;
	public static final int PlayerStatsInitPacket = 35;
	public static final int PlayerStatsUpdatePacket = 36;
	public static final int PlayerLivesUpdatePacket = 37;
	public static final int PreloadDataPacket = 50;
	public static final int WaveInitPacket = 60;
	public static final int WaveUpdatePacket = 61;
	public static final int ChatMessagePacket = 70;
	public static final int GlobalMessagePacket = 75;
	public static final int TowerBuildPacket = 81;
	public static final int TowerTargetPacket = 82;
	public static final int TowerAttackPacket = 83;
	public static final int TowerBuildRejectedPacket = 84;
	public static final int TutorialUpdatePacket = 85;
	public static final int TowerAreaRestrictionPacket = 88;
	public static final int GameDefeatPacket = 90;
	public static final int GameVictoryPacket = 91;
	public static final int GUIElementUpdatePacket = 99;

}
