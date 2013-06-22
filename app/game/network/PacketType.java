package game.network;

/**
 * The PacketType class contains all packet type declarations from client and
 * server. The value should be unique because the packet handler needs something
 * to identify the packet.
 * 
 * @author Sebastian Sachtleben
 */
public class PacketType {

  // Client
  public static final Integer ClientInitPacket = 1;
  public static final Integer ClientPreloadUpdatePacket = 10;
  public static final Integer ClientChatMessagePacket = 71;
  public static final Integer ClientTowerRequestPacket = 80;
  public static final Integer ClientWaveRequestPacket = 95;

  // Server
  public static final Integer AccessDeniedPacket = 11;
  public static final Integer AccessGrantedPacket = 12;
  public static final Integer GameStartPacket = 13;
  public static final Integer UnitInPacket = 16;
  public static final Integer UnitOutPacket = 17;
  public static final Integer PlayerLeavePacket = 21;
  public static final Integer PlayerInitPacket = 22;
  public static final Integer PlayerStatsInitPacket = 35;
  public static final Integer PlayerStatsUpdatePacket = 36;
  public static final Integer PlayerLivesUpdatePacket = 37;
  public static final Integer PreloadDataPacket = 50;
  public static final Integer WaveInitPacket = 60;
  public static final Integer WaveUpdatePacket = 61;
  public static final Integer ChatMessagePacket = 70;
  public static final Integer TowerBuildPacket = 81;
  public static final Integer TowerTargetPacket = 82;
  public static final Integer TowerAttackPacket = 83;
  public static final Integer TutorialUpdatePacket = 85;
  public static final Integer GameDefeatPacket = 90;
  public static final Integer GameVictoryPacket = 91;

}
