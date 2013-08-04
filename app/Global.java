import game.GamesHandler;
import game.network.GameServer;
import game.network.handler.WebSocketHandler;
import importer.EnvironmentImporter;
import importer.TowerImporter;
import importer.UnitImporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import models.entity.SecurityRole;
import models.entity.game.LevelRange;
import models.entity.game.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;

import com.ssachtleben.play.plugin.auth.models.PasswordUsernameAuthUser;

import dao.NewsDAO;
import dao.SecurityRoleDAO;
import dao.UserDAO;
import dao.game.MapDAO;

/**
 * Handles global settings.
 * 
 * @author Sebastian Sachtleben
 */
public class Global extends GlobalSettings {
	private static final Logger.ALogger log = Logger.of(Global.class);

	@Override
	public void onStart(Application app) {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				createLevelRanges();
				initialSecurityRoles();
				initGameServer();
				EnvironmentImporter.getInstance().sync();
				TowerImporter.getInstance().sync();
				UnitImporter.getInstance().sync();
				WebSocketHandler.getInstance();
				GamesHandler.getInstance();
				createAdminUser();
				createDummyNews();
				createMaps();
				Logger.info("Herowar has stated");
			}
		});
	}

	private void initGameServer() {
		GameServer.getInstance().start();
		Logger.info("GameServer started");
	}

	@Override
	public void onStop(Application app) {
		Logger.info("Herowar shutdown...");
		GameServer.getInstance().shutdown();
	}

	private void initialSecurityRoles() {
		if (SecurityRoleDAO.getSecurityRoleCount() != 0) {
			return;
		}
		Logger.info("Creating security roles");
		for (final String roleName : Arrays.asList(controllers.Application.ADMIN_ROLE, controllers.Application.USER_ROLE)) {
			final SecurityRole role = new SecurityRole();
			role.setRoleName(roleName);
			JPA.em().persist(role);
			Logger.info("Save role: " + role.getName());
		}
	}

	private void createAdminUser() {
		if (UserDAO.findByUsername("admin") != null) {
			Logger.info("Admin already exists!");
			return;
		}
		Logger.info("Creating admin user");
		UserDAO.create(new PasswordUsernameAuthUser("admin", "admin"), "admin@herowar.com", "admin", "admin");
	}

	private void createDummyNews() {
		if (!Play.application().isDev() || NewsDAO.getNewsCount() != 0) {
			return;
		}
		Logger.info("Creating dummy news");
		NewsDAO
				.create(
						"Lorem ipsum dolor sit amet",
						"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
						UserDAO.findByUsername("admin"));
	}

	private void createMaps() {
		Map map = MapDAO.getMapById(102L);
		if (map == null) {
			createMapFromSQL("pathToExil.sql");
		}
		map = MapDAO.getMapById(101L);
		if (map == null) {
			createMapFromSQL("tutorial.sql");
		}
	}

	private void createMapFromSQL(String filename) {
		BufferedReader bReader = null;
		Session sess = (Session) JPA.em().getDelegate();
		try {
			File file = Play.application().getFile("external" + File.separator + "sql" + File.separator + "maps" + File.separator + filename);
			bReader = new BufferedReader(new FileReader(file));
			StringBuffer sql = new StringBuffer();
			String line;
			while ((line = bReader.readLine().trim()) != null) {
				if (line.startsWith("--") || line.isEmpty())
					continue;

				sql.append(line);
				if (line.endsWith(");")) {
					final String tmp = sql.toString();

					sql = new StringBuffer();
					log.info(String.format("Process SQL: %s", tmp));
					sess.doWork(new Work() {
						@Override
						public void execute(Connection connection) throws SQLException {
							connection.createStatement().execute(tmp);
						}
					});
				}

			}
		} catch (Exception ex) {
			// log.error("Couldn't execute " + sqlFileOnClasspath, ex);
		} finally {
			if (bReader != null)
				try {
					bReader.close();
				} catch (IOException e) {

				}
		}
	}

	private void createLevelRanges() {
		LevelRange range = JPA.em().find(LevelRange.class, 1L);
		if (range != null) {
			return;
		}
		Logger.info("Creating level ranges");
		JPA.em().persist(new LevelRange(5000L));
		JPA.em().persist(new LevelRange(15000L));
		JPA.em().persist(new LevelRange(50000L));
		JPA.em().persist(new LevelRange(250000L));
		JPA.em().persist(new LevelRange(750000L));
	}
}
