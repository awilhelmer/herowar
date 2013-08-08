package com.herowar.jobs;

import java.util.Arrays;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.dao.NewsDAO;
import com.herowar.dao.SecurityRoleDAO;
import com.herowar.dao.UserDAO;
import com.herowar.models.entity.SecurityRole;
import com.herowar.models.entity.game.LevelRange;
import com.ssachtleben.play.plugin.auth.models.PasswordUsernameAuthUser;
import com.ssachtleben.play.plugin.cron.annotations.StartJob;
import com.ssachtleben.play.plugin.cron.jobs.Job;


/**
 * Imports example data.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true)
public class ExampleDataImporter implements Job {
	private static final Logger.ALogger log = Logger.of(ExampleDataImporter.class);

	@Override
	public void run() {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				createLevelRanges();
				initialSecurityRoles();
				createAdminUser();
				createDummyNews();
			}
		});
	}

	private void initialSecurityRoles() {
		if (SecurityRoleDAO.getSecurityRoleCount() != 0) {
			return;
		}
		log.info("Creating security roles");
		for (final String roleName : Arrays.asList(com.herowar.controllers.Application.ADMIN_ROLE, com.herowar.controllers.Application.USER_ROLE)) {
			final SecurityRole role = new SecurityRole();
			role.setRoleName(roleName);
			JPA.em().persist(role);
			log.info("Save role: " + role.getName());
		}
	}

	private void createAdminUser() {
		if (UserDAO.findByUsername("admin") != null) {
			log.info("Admin already exists!");
			return;
		}
		log.info("Creating admin user");
		UserDAO.create(new PasswordUsernameAuthUser("admin", "admin"), "admin@herowar.com", "admin", "admin");
	}

	private void createDummyNews() {
		if (NewsDAO.getNewsCount() != 0) {
			return;
		}
		log.info("Creating dummy news");
		NewsDAO
				.create(
						"Lorem ipsum dolor sit amet",
						"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
						UserDAO.findByUsername("admin"));
	}

	private void createLevelRanges() {
		LevelRange range = JPA.em().find(LevelRange.class, 1L);
		if (range != null) {
			return;
		}
		log.info("Creating level ranges");
		JPA.em().persist(new LevelRange(5000L));
		JPA.em().persist(new LevelRange(15000L));
		JPA.em().persist(new LevelRange(50000L));
		JPA.em().persist(new LevelRange(250000L));
		JPA.em().persist(new LevelRange(750000L));
	}
}
