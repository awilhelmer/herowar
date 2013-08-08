import sbt._
import collection.mutable.Map
import java.io.File.{ separator => / }

/**
 * JavascriptFilter removes all concated source files. This is currently a bit ugly since the files are already transfered to
 * classes folder so we just delete those without cache number in, but they should filtered properly during copy process.
 *
 * @author Sebastian Sachtleben
 */
trait JavascriptFilter {

  def filterResources(list1: Seq[(java.io.File, java.io.File)], list2: Seq[(java.io.File, java.io.File)], cacheNumber: String): Seq[(java.io.File, java.io.File)] = {
    filterResourceFolder(list1 ++ list2, cacheNumber)
    Seq.empty[(java.io.File, java.io.File)]
  }

  def filterResourceFolder(list: Seq[(java.io.File, java.io.File)], cacheNumber: String) = {
    var (cutPath, distPath, vendorsContent) = ("javascripts" + / + "vendors", "", Map[String, String]())
    for ((srcFile, destFile) <- list) {
      if (isSourceJsFile(destFile, cacheNumber)) {
        if (isVendorFile(destFile)) {
          // TODO: This is ugly !!! Start --Problem worng distPath
          if (distPath == "") {
            distPath = destFile.getAbsolutePath.substring(0, destFile.getAbsolutePath.indexOf(cutPath) + cutPath.length)
          }
          // TODO: This is ugly !!! End
          val relativePath = destFile.getAbsolutePath.substring(destFile.getAbsolutePath.indexOf(cutPath) + cutPath.length + 1)
          val key = relativePath.substring(0, relativePath.indexOf(/))
          vendorsContent.put(key, vendorsContent.get(key).getOrElse("") + FileUtils.fileToString(destFile, "UTF-8") + "\n")
        }
        if (!(destFile.delete))
          println("Problem on deleting File: " + destFile.getAbsolutePath)
      }
    }
    writeVendorsContent(distPath, vendorsContent, cacheNumber);
  }

  def isSourceJsFile(file: java.io.File, cacheNumber: String): Boolean = {
    file.exists && file.getName.endsWith(".js") && file.getName.indexOf(cacheNumber) == -1
  }

  def isVendorFile(file: java.io.File): Boolean = {
    // TODO: we need application mode here !!!
    file.getAbsolutePath.indexOf("javascripts" + / + "vendors") != -1 && file.getAbsolutePath.indexOf(".min.") == -1
  }

  def writeVendorsContent(path: String, content: Map[String, String], cacheNumber: String) = {
    for ((key, fileContent) <- content) {
      val fileName = path.replace(/ + "vendors", "") + / + key.substring(0, 1) + "v" + cacheNumber + ".js"
      val file = new File(fileName)
      //TODO Bad expensive check ...
      if (!(file.exists)) {
        println("Write file: " + fileName)
        FileUtils.writeFile(file, fileContent, "UTF-8")
      }
    }
  }

}