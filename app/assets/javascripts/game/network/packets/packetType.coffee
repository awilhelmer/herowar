packetType =

  # Client Packets
	CLIENT_AUTH			         		: 1
	CLIENT_PRELOAD_UPDATE	  		: 10
	CLIENT_CHAT_MESSAGE         : 71
	CLIENT_TOWER_REQUEST				: 80
	CLIENT_TUTORIAL_UPDATE			: 86
	CLIENT_WAVE_REQUEST         : 95

	# Server Packets
	SERVER_ACCESS_DECLINED    	: 11
	SERVER_ACCESS_GRANTED     	: 12
	SERVER_GAME_START						: 13
	SERVER_UNIT_IN   						: 16
	SERVER_UNIT_OUT	   					: 17
	SERVER_PLAYER_INIT_STATS		: 35
	SERVER_PLAYER_UPDATE_STATS	: 36
	SERVER_PLAYER_UPDATE_LIVES	: 37
	SERVER_PRELOAD_DATA					: 50
	SERVER_WAVE_INIT						: 60
	SERVER_WAVE_UPDATE					: 61
	SERVER_CHAT_MESSAGE					: 70
	SERVER_BUILD_TOWER					: 81
	SERVER_TARGET_TOWER					: 82
	SERVER_ATTACK_TOWER					: 83
	SERVER_TUTORIAL_UPDATE			: 85
	SERVER_GAME_DEFEAT					: 90
	SERVER_GAME_VICTORY					: 91
	SERVER_GUI_UPDATE						: 99

return packetType