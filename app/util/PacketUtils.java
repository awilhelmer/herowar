package util;

import game.network.server.PreloadData;
import game.network.server.PreloadDataPacket;
import game.processor.GameProcessor;

import java.util.HashMap;
import java.util.Iterator;

import models.entity.game.Tower;
import models.entity.game.TowerWeapon;
import models.entity.game.TowerWeaponType;
import models.entity.game.Unit;
import models.entity.game.Wave;

import org.webbitserver.WebSocketConnection;

public class PacketUtils {

	public static PreloadDataPacket createPreloadDataPacket(final WebSocketConnection connection, final GameProcessor game) {
		if (game.getPreloadPacket() == null) {
			java.util.Map<String, String> images = new HashMap<String, String>();
			java.util.Map<String, String> textures = new HashMap<String, String>();
			textures.put("ground-rock", "assets/images/game/textures/ground/rock.jpg");
			textures.put("ground-grass", "assets/images/game/textures/ground/grass.jpg");

			textures.put("texture_ground_grass", "assets/images/game/textures/ground/texture_ground_grass.jpg");
			textures.put("texture_ground_bare", "assets/images/game/textures/ground/texture_ground_bare.jpg");
			textures.put("texture_ground_snow", "assets/images/game/textures/ground/texture_ground_snow.jpg");
			// textures.put("stone-natural-001", "assets/images/game/textures/stone/natural-001.jpg");
			// textures.put("stone-rough-001", "assets/images/game/textures/stone/rough-001.jpg");
			java.util.Map<String, String> texturesCube = new HashMap<String, String>();
			if (game.getMap().getSkybox() != null && !"".equals(game.getMap().getSkybox())) {
				String skybox = game.getMap().getSkybox();
				texturesCube.put(skybox, "assets/images/game/skybox/" + skybox + "/%1.jpg");
			}
			java.util.Map<String, String> geometries = new HashMap<String, String>();
			Iterator<Wave> iter = game.getMap().getWaves().iterator();
			while (iter.hasNext()) {
				Wave wave = iter.next();
				Iterator<Unit> iter2 = wave.getUnits().iterator();
				while (iter2.hasNext()) {
					Unit unit = iter2.next();
					geometries.put(unit.getName(), "api/game/geometry/unit/" + unit.getId());
					if (unit.getExplode() && !images.containsKey("explosion")) {
						images.put("explosion", "assets/images/game/textures/effects/explosion.png");
					}
					if (unit.getBurning() && !images.containsKey("cloud10")) {
						textures.put("cloud10", "assets/images/game/textures/effects/cloud10.png");
					}
				}
			}
			Iterator<Tower> iter3 = game.getMap().getTowers().iterator();
			while (iter3.hasNext()) {
				Tower tower = iter3.next();
				geometries.put(tower.getName(), "api/game/geometry/tower/" + tower.getId());
				for (TowerWeapon weapon : tower.getWeapons()) {
					if (TowerWeaponType.LASER.equals(weapon.getType()) && !geometries.containsKey("particle001")) {
						textures.put("particle001", "assets/images/game/textures/effects/particle001.png");
					}
					if (TowerWeaponType.ROCKET.equals(weapon.getType()) && !geometries.containsKey("rocket")) {
						geometries.put("rocket", "assets/geometries/weapons/rocket.js");
					}
					if (TowerWeaponType.ROCKET.equals(weapon.getType()) && !images.containsKey("explosion")) {
						images.put("explosion", "assets/images/game/textures/effects/explosion.png");
					}
				}
			}
			game.setPreloadPacket(new PreloadDataPacket(game.getMap().getId(), new PreloadData(images, textures, texturesCube, geometries)));
		}
		return game.getPreloadPacket();
	}

}
