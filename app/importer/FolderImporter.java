package importer;

import java.io.File;
import java.io.Serializable;

import play.Play;
import play.db.jpa.JPA;

/**
 * The FolderImporter runs over the base folder and import and update each geometry within this folder.
 * 
 * @author Sebastian Sachtleben
 * 
 * @param <E>
 *          The entity which will be created.
 */
public abstract class FolderImporter<E extends Serializable> extends AbstractImporter<E> {

	@Override
	public void process() {
		File baseFolder = new File(Play.application().path(), getBaseFolder());
		readDirectory(baseFolder, null, true);
	}

	@Override
	protected E createEntry(String name, E parent) {
		E entry = super.createEntry(name, parent);
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
