import sbt._
import Keys._
import play.Project._
import collection.mutable.Map

/**
 * JavascriptTransformer transforms javascript files and concat them to several big files.
 *
 * @author Sebastian Sachtleben
 */
trait JavascriptTransformer {
  val pattern = """(\A\(function\(\)[\s]?\{)|(\}\)\.call\(this\)\;[\n+|\s+]*\z)"""
  val (scripts_folder, templates_folder, vendors_folder) = ("scripts", "templates", "vendors")
  val content = Map[(String, String, String), String]()
  var loader = ""
  // This takes the raw resources, which are the .css files and  the .js files from coffeescript and handlebars.  It separates
  //  the .js files from the .css files and transforms just the .js files.
  def transformResources(classDirectory: java.io.File, original: Seq[java.io.File], cacheNumber: String): Seq[java.io.File] = {
    val (js, nonJs) = original.partition(_.getName.endsWith(".js"))
    nonJs ++ transformJs(classDirectory, js, cacheNumber)
  }

  // This takes the list of all .js files.  It should transform them into new files, such as by concatenating them and writing 
  // them to new files. The list of new files should be returned.
  def transformJs(classDirectory: java.io.File, jsFiles: Seq[java.io.File], cacheNumber: String): Seq[java.io.File] = {
    var (distPath, cutPath) = ("", "javascripts\\")
    //content Map Keyorder: JS-Type, part of application, buildMode  

    jsFiles.map(f => {
      //TODO: getting Path of actual Module from Build Task ?  
      val relativePath = f.getAbsolutePath.substring(f.getAbsolutePath.indexOf(cutPath) + cutPath.length)

      // TODO: This is ugly !!! Start --Problem worng distPath
      if (distPath == "") {
        distPath = f.getAbsolutePath.substring(0, f.getAbsolutePath.indexOf(cutPath) + cutPath.length)
      }
      // TODO: This is ugly !!! End

      // Match relative path and save content
      relativePath match {

        // Special case for loader.js file, save content to loaderMin variable
        case "loader.js" | "loader.min.js" => {
          if ((loader == "") && (isModeFile(relativePath))) {
            loader = FileUtils.fileToString(f, "UTF-8").replaceAll(pattern, "")
          }
        }
        // Parse every file to content map
        case _ => {
          val jsType = if (relativePath.indexOf(templates_folder) == 0) templates_folder else if (relativePath.indexOf(vendors_folder) == 0) vendors_folder else scripts_folder
          var key = ""
          var preFixFunction = ""
          // TODO: Is it possible to do this better? For templates we need to get the second folder name. Maybe with regex...
          if (jsType != "templates") {
            key = relativePath.substring(0, relativePath.indexOf('\\'))
          } else {
            val tempPath = relativePath.substring(relativePath.indexOf('\\') + 1)
            key = tempPath.substring(0, tempPath.indexOf('\\'))
            preFixFunction = "return"
          }
          // TODO: end 
          if (!(new File( distPath + key.substring(0, 1) + jsType.substring(0, 1) + cacheNumber + ".js").exists())) {
            var subfolders = relativePath.substring(relativePath.indexOf(key) + key.length(), relativePath.lastIndexOf('\\'));
            if (subfolders.length > 0)
              subfolders = subfolders.substring(1) + '\\'
            val functionName = subfolders.replaceAll("""\\""", "/") + f.getName().substring(0, f.getName().lastIndexOf("."))
            val mapKey = (jsType, key, ApplicationBuild.buildMode)
            val mappedContent = mapContent(functionName, FileUtils.fileToString(f, "UTF-8"), preFixFunction);
            if (isModeFile(functionName)) {
              content.put((mapKey), content.get(mapKey).getOrElse("") + mappedContent + "\n")
            }
          }
        }
      }
    })

    // Check if loader exists
    if (loader == "") throw new Exception("Couldn't find loader in root javascript folder!")

    // Write content to file system
    writeCombinedFiles(distPath, cacheNumber, content, loader)
  }

  /**
   * Write combined files to output generated from Map[(String, String, String), String]].
   */
  def writeCombinedFiles(path: String, cacheNumber: String, content: Map[(String, String, String), String], loader: String): Seq[File] = {
    var writtenFiles = Seq.empty[File]
    for ((tuple, entries) <- content) {
      val fileName = path + tuple._2.substring(0, 1) + tuple._1.substring(0, 1) + cacheNumber + ".js"
      val file = new File(fileName)
      if (!(file.exists())) {
        println("Write file: " + fileName)
        var fileContent = ""
        if (tuple._1 == scripts_folder) {
          fileContent = loader;
        }
        fileContent += content(tuple);
        writtenFiles = writtenFiles ++ Seq(FileUtils.writeFile(file, fileContent, "UTF-8"))
      }
    }
    writtenFiles
  }

  /**
   * Wraps content into a define
   */
  def mapContent(module: String, content: String, preFixFunction: String): String = {
    "define('%s',function() {%s %s});".format(module, preFixFunction, content.replaceAll(pattern, ""))
  }

  def isModeFile(name: String): Boolean = {
    var result = false;
    if (((ApplicationBuild.buildMode == "dev") && (name.indexOf(".min") == -1))
      || ((ApplicationBuild.buildMode == "prod") && (name.indexOf(".min") > -1)))
      result = true;
    result;
  }
}
