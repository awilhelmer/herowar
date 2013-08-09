package com.herowar.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;

import com.herowar.dao.MapDAO;
import com.herowar.models.entity.game.Map;
import com.ssachtleben.play.plugin.cron.annotations.DependsOn;
import com.ssachtleben.play.plugin.cron.annotations.StartJob;
import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * Imports example maps.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true, active = true)
@DependsOn(values = { ExampleDataImporter.class, EnvironmentImporter.class, TowerImporter.class, UnitImporter.class })
public class ExampleMapImporter implements Job {
	private static final Logger.ALogger log = Logger.of(ExampleMapImporter.class);

	@Override
	public void run() {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Map map = MapDAO.getMapById(102L);
				if (map == null) {
					createMapFromSQL("pathToExil.sql");
				}
				map = MapDAO.getMapById(101L);
				if (map == null) {
					createMapFromSQL("tutorial.sql");
				}
			}
		});
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

}
