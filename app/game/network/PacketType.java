package game.network;


/**
 * Several packet type ids.
 * 
 * @author Sebastian Sachtleben
 */
public class PacketType {

  // Client
  public static final Integer ClientInitPacket = 1;
  public static final Integer ClientPreloadUpdatePacket = 10;
//  public static final Integer ClientRotationPacket = 2;
//  public static final Integer ClientShot1Packet = 5;
//  public static final Integer ClientInputKeyPacket = 6;

  // Server
  public static final Integer AccessDeniedPacket = 11;
  public static final Integer AccessGrantedPacket = 12;
  public static final Integer GameStartPacket = 13;
//  public static final Integer ObjectDamagePacket = 19;
//  public static final Integer ObjectInPacket = 16;
//  public static final Integer ObjectOutPacket = 17;
//  public static final Integer PlayerJoinPacket = 20;
  public static final Integer PlayerLeavePacket = 21;
  public static final Integer PlayerInitPacket = 22;
  public static final Integer PlayerStatsInitPacket = 35;
  public static final Integer PlayerStatsUpdatePacket = 36;
//  public static final Integer ObjectPositionPacket = 26;
//  public static final Integer PlayerInputPacket = 23;
//  public static final Integer ObjectRotationPacket = 30;
//  public static final Integer ObjectShotPacket = 31;
  public static final Integer PreloadDataPacket = 50;
//  public static final Integer ChatMessagePacket = 70;
  public static final Integer WaveUpdatePacket = 60;
  

}
