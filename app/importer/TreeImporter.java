package importer;

import java.io.File;
import java.io.Serializable;

import play.Play;
import play.db.jpa.JPA;

/**
 * The TreeImporter runs over the base folder recursivly and import and update
 * each geometry within this folder.
 * 
 * @author Sebastian Sachtleben
 * 
 * @param <E>
 *          The entity which will be created.
 */
public abstract class TreeImporter<E extends Serializable> extends AbstractImporter<E> {

  @Override
  public void process() {
    File baseFolder = new File(Play.application().path(), getBaseFolder());
    E root = createEntry("Root", null);
    if (root != null) {
      readDirectory(baseFolder, root, true);
      if (!JPA.em().contains(root)) {
        JPA.em().persist(root);
      } else {
        root = JPA.em().merge(root);
      }
    }
  }

}
