package com.herowar.jobs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.db.jpa.JPA;

import com.herowar.dao.game.GeometryDAO;
import com.herowar.dao.game.MaterialDAO;
import com.herowar.models.entity.game.Geometry;
import com.herowar.models.entity.game.Material;
import com.herowar.util.JsonUtils;
import com.ssachtleben.play.plugin.cron.jobs.Job;
import com.ssachtleben.play.plugin.imports.PathImporter;

/**
 * Provides important functions to import and update entity objects.
 * 
 * @author Sebastian Sachtleben
 * 
 * @param <E>
 *          The entity to import.
 */
public abstract class EntityImporter<E extends Serializable> extends PathImporter<E> implements Job {

	private static ObjectMapper mapper = new ObjectMapper();
	private Class<E> clazz;
	private boolean updateGeo;

	/**
	 * Creates new {@link EntityImporter} instance.
	 */
	public EntityImporter() {
		super();
		this.clazz = getTypeParameterClass();
		BeanUtilsBean.setInstance(new BeanUtilsBean(new EnumAwareConvertUtilsBean()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				log().info("Starting synchronize between folder and database");
				process();
				log().info("Finish synchronize between folder and database");
			}
		});
	}

	protected void readDirectory(File folder, E parent, boolean recursive) {
		try {
			for (File file : folder.listFiles(new JsFileFilter())) {
				E entity = createEntry(file, parent);
				updateGeo = false;
				if (file.isDirectory() && recursive) {
					log().info("Found directory: " + file.getAbsolutePath());
					readDirectory(file, entity, recursive);
					updateGeo = true;
				} else {
					log().info("Found geometry: " + file.getAbsolutePath());
					updateEntity(file, entity);
				}
				saveEntity(entity, parent);
			}

		} catch (Exception e) {
			log().error("", e);
		}
	}

	protected void updateEntity(File file, E model) {
		try {
			if (PropertyUtils.isReadable(model, "geometry")) {
				Geometry geo = (Geometry) PropertyUtils.getProperty(model, "geometry");
				if (geo == null || geo.getUdate() == null || geo.getUdate().getTime() < file.lastModified()) {
					Geometry newGeo = parseGeometryFile(file);
					newGeo = syncGeometry(geo, newGeo);
					PropertyUtils.setProperty(model, "geometry", newGeo);
					updateGeo = true;
				}
			} else {
				log().warn(String.format("Property geometry not found on class <%s>", model.getClass()));
			}
			updateByOpts(file, model);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log().error(String.format("Failed to update %s with %s", file, model), e);
		}
	}

	private void updateByOpts(File file, E model) {
		try {
			File optsFile = new File(file.getAbsolutePath().replace(".js", ".opts"));
			log().info("Looking for opts file: " + optsFile);
			if (!optsFile.exists() || !optsFile.isFile()) {
				return;
			}
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(optsFile);
			JsonUtils.parse(model, node);
		} catch (IOException e) {
			log().error(String.format("Failed to update via opts file %s with %s", file, model), e);
		}

	}

	@SuppressWarnings("unchecked")
	protected void saveEntity(E entity, E parent) {
		try {
			if (PropertyUtils.isReadable(entity, "children") && updateGeo) {
				if (parent != null) {
					Collection<E> children = (Collection<E>) PropertyUtils.getProperty(parent, "children");
					Object id = PropertyUtils.getProperty(entity, "id");
					if (id != null && JPA.em().contains(entity)) {
						entity = JPA.em().merge(entity);
					} else if (JPA.em().contains(parent)) {
						JPA.em().persist(entity);
					}
					children.add(entity);
				}

			} else if (!updateGeo) {
				log().warn(String.format("Property children not found on class <%s>", entity.getClass()));
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log().error(String.format("Failed to save %s from %s", entity, parent), e);
		}
	}

	private Geometry syncGeometry(Geometry geo, Geometry newGeo) {
		if (geo != null && geo.getId() != null) {
			log().info("Sync geo Id: " + geo.getId());
			newGeo.setId(geo.getId());
			newGeo.setVersion(geo.getVersion());
			if (newGeo.getGeoMaterials().equals(geo.getGeoMaterials())) {
				newGeo.setGeoMaterials(geo.getGeoMaterials());
			}
			newGeo.getMetadata().setId(geo.getMetadata().getId());
			newGeo.setMetadata(JPA.em().merge(newGeo.getMetadata()));
			newGeo.setMeshes(geo.getMeshes());
			newGeo.setCdate(geo.getCdate());
			newGeo = JPA.em().merge(newGeo);
		}
		return newGeo;
	}

	protected E createEntry(File file, E parent) {
		try {
			return createEntry(WordUtils.capitalize(file.getName().replace(".js", "")), parent);
		} catch (Exception e) {
			log().error("", e);
		}
		return null;
	}

	protected E createEntry(String name, E parent) {
		E entry = null;
		entry = getByName(clazz, name);
		if (entry == null) {
			try {
				entry = clazz.newInstance();
				if (PropertyUtils.isReadable(entry, "name")) {
					PropertyUtils.setProperty(entry, "name", name);
				} else {
					log().warn(String.format("Property name not found on class <%s>", entry.getClass()));
				}
				if (parent != null && PropertyUtils.isReadable(entry, "parent")) {
					PropertyUtils.setProperty(entry, "parent", parent);
				} else {
					log().warn(String.format("Property parent not found on class <%s>", entry.getClass()));
				}
			} catch (Exception e) {
				log().error("", e);
			}
		}
		return entry;
	}

	private E getByName(Class<E> clazz, String name) {
		E result = null;
		CriteriaBuilder builder = JPA.em().getCriteriaBuilder();
		CriteriaQuery<E> q = builder.createQuery(clazz);
		Root<E> root = q.from(clazz);
		q.where(builder.equal(root.get("name"), name));
		try {
			result = JPA.em().createQuery(q).getSingleResult();
		} catch (NoResultException e) {
			// nothing
		}
		return result;
	}

	private Geometry parseGeometryFile(File file) {
		try {
			Geometry geo = mapper.readValue(file, Geometry.class);
			int sortIndex = 0;
			for (Material mat : geo.getMaterials()) {
				mat.setName(mat.getDbgName());
				mat.setSortIndex(sortIndex++);
			}
			Map<Integer, Material> matMap = MaterialDAO.mapAndSave(geo.getMaterials());
			GeometryDAO.createGeoMaterials(geo, matMap);
			return geo;
		} catch (IOException e) {
			log().error("Failed to parse geometry file:", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Class<E> getTypeParameterClass() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<E>) paramType.getActualTypeArguments()[0];
	}

	@Override
	protected boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".js");
	}

	/**
	 * The JsFileFilter filters for js files.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public class JsFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return EntityImporter.this.accept(pathname);
		}

	}
}
