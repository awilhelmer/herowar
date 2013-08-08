package com.herowar.jobs;

import java.io.File;

import play.db.jpa.JPA;

import com.herowar.models.entity.game.Tower;
import com.ssachtleben.play.plugin.cron.annotations.StartJob;

/**
 * Syncronizes javascript files from "public/geometries/towers" with {@link Tower} models.
 * 
 * @author Sebastian Sachtleben
 */
@StartJob(async = true, active = true)
public class TowerImporter extends EntityImporter<Tower> {

	@Override
	public String getBaseFolder() {
		return "public" + File.separator + "geometries" + File.separator + "towers";
	}
	
	@Override
	protected Tower createEntry(String name, Tower parent) {
		Tower entry = super.createEntry(name, parent);
		if (entry != null) {
			if (!JPA.em().contains(entry)) {
				JPA.em().persist(entry);
			} else {
				entry = JPA.em().merge(entry);
			}
		}
		return entry;
	}

}
