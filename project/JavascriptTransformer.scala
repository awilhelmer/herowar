import sbt._
import Keys._
import play.Project._
import collection.mutable.Map

/**
 * JavascriptTransformer transforms javascript files and concat them to several big files.
 *
 * @author Sebastian Sachtleben
 */
trait JavascriptTransformer extends FileUtils {
  val buildModes = List("dev", "prod")

  // This takes the raw resources, which are the .css files and  the .js files from coffeescript and handlebars.  It separates
  //  the .js files from the .css files and transforms just the .js files.
  def transformResources(original: Seq[File]): Seq[File] = {
    val (js, nonJs) = original.partition(_.getName.endsWith(".js"))
    nonJs ++ transformJs(js)
  }

  // This takes the list of all .js files.  It should transform them into new files, such as by concatenating them and writing 
  // them to new files. The list of new files should be returned.
  def transformJs(jsFiles: Seq[File]): Seq[File] = {
    println("transformJs")
    var (loader, loaderMin, distPath, cutPath, content) = ("", "", "", "javascripts\\", Map[(String, String, String), String]())
    //content Map Keyorder: JS-Type, part of application, buildMode  

    jsFiles.map(f => {
      val relativePath = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(cutPath) + cutPath.length)

      // TODO: This is ugly !!! Start
      if (distPath == "") {
        distPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf(cutPath) + cutPath.length)
      }
      // TODO: This is ugly !!! End

      // Match relative path and save content
      relativePath match {

        // Special case for loader.js file, save content to loader variable
        case "loader.js" => loader = fileToString(f, "utf-8")

        // Special case for loader.min.js file, save content to loaderMin variable
        case "loader.min.js" => loaderMin = fileToString(f, "utf-8")

        // Parse every file to content map
        case _ => {
          val (isTemplate, isLib, isScript, key) = (
            relativePath.indexOf("templates") == 0,
            relativePath.indexOf("libs") == 0,
            relativePath.indexOf("templates") != 0 && relativePath.indexOf("libs") != 0,
            relativePath.substring(0, relativePath.indexOf('\\')))
          val jsType = if (isTemplate) "templates" else if (isLib) "vendors" else if (isScript) "scripts" else "unknowned"
          // Check if map contains js type e.g. templates, vendors or scripts
          // Check if inner map contains key e.g. game or page
          val fileContent = fileToString(f, "UTF-8")
          for (mode <- buildModes) {
            content.put(((jsType, key, mode)), content.get((jsType, key, mode)).getOrElse("") + fileContent)
          }
        }
      }
    })

    // Write content to file system
    writeCombinedFiles(distPath, content)

    if (loader == "" || loaderMin == "") throw new Exception("Couldn't find loader in root javascript folder!")
    Seq.empty[File]
  }

  /**
   * Write combined files to output generated from Map[String, Map[String, String]].
   */
  def writeCombinedFiles(path: String, content: Map[(String, String, String), String]) = {
    for ((tuple, entries) <- content) {
      if (tuple._3 == "prod") {
        println("Output type: " + tuple._1)
        val fileName = path + tuple._1 + "_" + tuple._2 + ".js"
        println("Write file:" + fileName)
        writeFile(new File(fileName), content(tuple), "UTF-8")
      }
    }

  }
}