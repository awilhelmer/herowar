import sbt._

/**
 * JavascriptFilter removes all concated source files. This is currently a bit ugly since the files are already transfered to 
 * classes folder so we just delete those without cache number in, but they should filtered properly during copy process.
 *
 * @author Sebastian Sachtleben
 */
trait JavascriptFilter {

  def filterResources(list1: Seq[(java.io.File, java.io.File)], list2: Seq[(java.io.File, java.io.File)], cacheNumber: String) : Seq[(java.io.File, java.io.File)] = {
    filterResourceFolder(list1 ++ list2, cacheNumber)
    Seq.empty[(java.io.File, java.io.File)]
  }

  def filterResourceFolder(list: Seq[(java.io.File, java.io.File)], cacheNumber: String) = {
    for ((srcFile, destFile) <- list) {
      if (destFile.exists() && destFile.getName.endsWith(".js") && destFile.getName.indexOf(cacheNumber) == -1) {
        destFile.delete
      }
    }
  }

}