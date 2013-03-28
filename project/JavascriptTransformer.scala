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
    var (loader, loaderMin, distPath, cutPath, content) = ("", "", "", "javascripts\\", Map[(String, String, String), String]())
    //content Map Keyorder: JS-Type, part of application, buildMode  

    jsFiles.map(f => {
      //TODO: getting Path of actual Module from Build Task ?  
      val relativePath = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(cutPath) + cutPath.length)

      // TODO: This is ugly !!! Start --Problem worng distPath
      if (distPath == "") {
        distPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf(cutPath) + cutPath.length)
      }
      // TODO: This is ugly !!! End

      val fileContent = fileToString(f, "UTF-8")
      // Match relative path and save content
      relativePath match {

        // Special case for loader.js file, save content to loader variable
        case "loader.js" => loader = fileContent

        // Special case for loader.min.js file, save content to loaderMin variable
        case "loader.min.js" => loaderMin = fileContent

        // Parse every file to content map
        case _ => {
          val (isTemplate, isLib, isScript, key) = (
            relativePath.indexOf("templates") == 0,
            relativePath.indexOf("libs") == 0,
            relativePath.indexOf("templates") != 0 && relativePath.indexOf("libs") != 0,
            relativePath.substring(0, relativePath.indexOf('\\')))
          val jsType = if (isTemplate) "templates" else if (isLib) "vendors" else if (isScript) "scripts" else "unknowned"
          val functionName = key + "." + f.getName().substring(0, f.getName().lastIndexOf("."))
          // Check if map contains js type e.g. templates, vendors or scripts
          // Check if inner map contains key e.g. game or page

          val mappedContent = mapContent(functionName, fileContent);
          if (((ApplicationBuild.buildMode == "dev") && (functionName.indexOf(".min") == -1)) 
              || ((ApplicationBuild.buildMode == "prod") && (functionName.indexOf(".min") > -1))) {
            for (mode <- buildModes) {
              if (ApplicationBuild.buildMode == mode)
                content.put(((jsType, key, mode)), content.get((jsType, key, mode)).getOrElse("") + mappedContent + "\n")
            }
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
   * Write combined files to output generated from Map[(String, String, String), String]].
   */
  def writeCombinedFiles(path: String, content: Map[(String, String, String), String]) = {
    for ((tuple, entries) <- content) {
      val fileName = path + tuple._1 + "_" + tuple._2 + ".js"
      println("Write file: " + fileName)
      writeFile(new File(fileName), content(tuple), "UTF-8")
    }
  }

  /**
   * Wraps content into a define
   */
  def mapContent(module: String, content: String): String = {
    val pattern = """(\A\(function\(\)[\s]?\{)|(\}\)\.call\(this\)\;[\n+|\s+]*\z)"""
    var replacedValue = content.replaceAll(pattern, "")
    if (replacedValue == "")
      replacedValue = content;
    "define('" + module + "', function() {" + replacedValue + "});"

  }
}
