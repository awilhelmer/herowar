package importer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.entity.game.Geometry;
import models.entity.game.Material;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import play.Application;
import play.Logger.ALogger;
import play.Plugin;
import play.db.jpa.JPA;
import play.libs.Akka;
import util.JsonUtils;
import dao.game.GeometryDAO;
import dao.game.MaterialDAO;

/**
 * The AbstractImporter provides basic functionality to import json js files to our database.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AbstractImporter<E extends Serializable> extends Plugin {

	private static ObjectMapper mapper = new ObjectMapper();

	private Class<E> clazz;
	private boolean updateGeo;
	private boolean async = false;

	public AbstractImporter(final Application app) {
		this.clazz = getTypeParameterClass();
		BeanUtilsBean.setInstance(new BeanUtilsBean(new EnumAwareConvertUtilsBean()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStart()
	 */
	@Override
	public void onStart() {
		if (async) {
			syncAsync();
		} else {
			sync();
		}
	}

	/**
	 * Syncronize data syncron.
	 */
	public void sync() {
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				getLogger().info("Starting synchronize between folder and database");
				process();
				getLogger().info("Finish synchronize between folder and database");
			}
		});
	}

	/**
	 * Syncronize data asyncron.
	 */
	public void syncAsync() {
		Akka.future(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				sync();
				return null;
			}
		});
	}

	protected void readDirectory(File folder, E parent, boolean recursive) {
		try {
			for (File file : folder.listFiles(new JsFileFilter())) {
				E entity = createEntry(file, parent);
				updateGeo = false;
				if (file.isDirectory() && recursive) {
					getLogger().info("Found directory: " + file.getAbsolutePath());
					readDirectory(file, entity, recursive);
					updateGeo = true;
				} else {
					getLogger().info("Found geometry: " + file.getAbsolutePath());
					updateEntity(file, entity);
				}
				saveEntity(entity, parent);
			}

		} catch (Exception e) {
			getLogger().error("", e);
		}
	}

	protected void updateEntity(File file, E model) throws Exception {
		if (PropertyUtils.isReadable(model, "geometry")) {
			Geometry geo = (Geometry) PropertyUtils.getProperty(model, "geometry");
			if (geo == null || geo.getUdate() == null || geo.getUdate().getTime() < file.lastModified()) {
				Geometry newGeo = parseGeometryFile(file);
				newGeo = syncGeometry(geo, newGeo);
				PropertyUtils.setProperty(model, "geometry", newGeo);
				updateGeo = true;
			}
		} else {
			getLogger().warn(String.format("Property geometry not found on class <%s>", model.getClass()));
		}
		updateByOpts(file, model);
	}

	private void updateByOpts(File file, E model) throws JsonProcessingException, IOException {
		File optsFile = new File(file.getAbsolutePath().replace(".js", ".opts"));
		getLogger().info("Looking for opts file: " + optsFile);
		if (!optsFile.exists() || !optsFile.isFile()) {
			return;
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(optsFile);
		JsonUtils.parse(model, node);
	}

	@SuppressWarnings("unchecked")
	protected void saveEntity(E entity, E parent) throws Exception {
		if (PropertyUtils.isReadable(entity, "children") && updateGeo) {
			Collection<E> children = (Collection<E>) PropertyUtils.getProperty(parent, "children");
			Object id = PropertyUtils.getProperty(entity, "id");
			if (id != null && JPA.em().contains(entity)) {
				entity = JPA.em().merge(entity);
			} else if (JPA.em().contains(parent)) {
				JPA.em().persist(entity);
			}
			children.add(entity);

		} else if (!updateGeo) {
			getLogger().warn(String.format("Property children not found on class <%s>", entity.getClass()));
		}
	}

	private Geometry syncGeometry(Geometry geo, Geometry newGeo) {
		if (geo != null && geo.getId() != null) {
			getLogger().info("Sync geo Id: " + geo.getId());
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
			getLogger().error("", e);
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
					getLogger().warn(String.format("Property name not found on class <%s>", entry.getClass()));
				}
				if (parent != null && PropertyUtils.isReadable(entry, "parent")) {
					PropertyUtils.setProperty(entry, "parent", parent);
				} else {
					getLogger().warn(String.format("Property parent not found on class <%s>", entry.getClass()));
				}
			} catch (Exception e) {
				getLogger().error("", e);
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
			getLogger().error("Failed to parse geometry file:", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Class<E> getTypeParameterClass() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<E>) paramType.getActualTypeArguments()[0];
	}

	public abstract void process();

	public abstract String getBaseFolder();

	protected abstract ALogger getLogger();

	protected abstract boolean accept(File file);

	/**
	 * The JsFileFilter filters for js files.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public class JsFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return AbstractImporter.this.accept(pathname);
		}

	}
}
