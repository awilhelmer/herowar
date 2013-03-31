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
  val (scripts_folder, templates_folder, vendors_folder, shared_folder) = ("scripts", "templates", "vendors", "shared")
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
    var (distPath, cutPath, content) = ("", "javascripts\\", Map[(String, String, String), String]())
    //content Map Keyorder: JS-Type, part of application, buildMode  
    jsFiles.map(f => {
      //TODO Check modifaction ...
      if (distPath == "") {
        distPath = f.getAbsolutePath.substring(0, f.getAbsolutePath.indexOf(cutPath) + cutPath.length)
      }
      val relativePath = f.getAbsolutePath.substring(f.getAbsolutePath.indexOf(cutPath) + cutPath.length)
      relativePath match {
        case "loader.js" | "loader.min.js" => {
          //Loader is static and shouldnt change
        }
        case _ => {
          val mapKeys = getMapKeys(relativePath)
          if ((FileCacheHandler.filesChanged.contains(f.getAbsolutePath))) {
            val lastTime = FileCacheHandler.filesChanged(f.getAbsolutePath)
            if (f.lastModified > lastTime) {
              for (mapKey <- mapKeys)
                FileCacheHandler.unchangedModules -= mapKey
            }
          }
          FileCacheHandler.filesChanged.put(f.getAbsolutePath, f.lastModified)
        }
      }

    })
    jsFiles.map(f => {
      //TODO: getting Path of actual Module from Build Task ?  
      val relativePath = f.getAbsolutePath.substring(f.getAbsolutePath.indexOf(cutPath) + cutPath.length)

      // TODO: This is ugly !!! Start --Problem worng distPath

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
          val mapKeys = getMapKeys(relativePath)
          var preFixFunction = ""
          for (mapKey <- mapKeys) {
            if (mapKey._1 == "templates") {
              preFixFunction = "return"
            }
            if (!(FileCacheHandler.unchangedModules.contains(mapKey))) {
              var subfolders = ""
              if (relativePath.indexOf(shared_folder) > -1) {
                subfolders = '\\' + relativePath.substring(relativePath.indexOf(shared_folder), relativePath.lastIndexOf('\\'));          
              }else {
                subfolders = relativePath.substring(relativePath.indexOf(mapKey._2) + mapKey._2.length(), relativePath.lastIndexOf('\\'));                
              }
              if (subfolders.length > 0)
                subfolders = subfolders.substring(1) + '\\'
              val functionName = subfolders.replaceAll("""\\""", "/") + f.getName().substring(0, f.getName().lastIndexOf("."))
              val mappedContent = mapContent(functionName, FileUtils.fileToString(f, "UTF-8"), preFixFunction);
              if (isModeFile(functionName)) {
                content.put((mapKey), content.get(mapKey).getOrElse("") + mappedContent + "\n")
              }
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
    for ((tuple, entries) <- content) {
      val fileName = path + tuple._2.substring(0, 1) + tuple._1.substring(0, 1) + cacheNumber + ".js"

      println("Write file: " + fileName)
      var fileContent = ""
      if (tuple._1 == scripts_folder) {
        fileContent = loader;
      }
      fileContent += content(tuple);
      val file = FileUtils.writeFile(new File(fileName), fileContent, "UTF-8")
      if (!(FileCacheHandler.writtenFiles.contains(file)))
        FileCacheHandler.writtenFiles :+= file
      FileCacheHandler.unchangedModules :+= tuple
      println(FileCacheHandler.unchangedModules.size)
    }
    FileCacheHandler.writtenFiles
  }

  /**
   * Wraps content into a define
   */
  def mapContent(module: String, content: String, preFixFunction: String): String = {
    var name = module;
    if (module.indexOf("shared/") == 0) {
      name = module.substring("shared/".length);
    }
    "define('%s',function() {%s %s});".format(name, preFixFunction, content.replaceAll(pattern, ""))
  }

  def getMapKeys(path: String): List[(String, String, String)] = {
    // TODO: Is it possible to do this better? For templates we need to get the second folder name. Maybe with regex...
    var result = List.empty[(String, String, String)]
    val jsType = if (path.indexOf(templates_folder) == 0) templates_folder else if (path.indexOf(vendors_folder) == 0) vendors_folder else scripts_folder
    var key = "";
    if (jsType != "templates") {
      key = path.substring(0, path.indexOf('\\'))
      if ((jsType == scripts_folder) && (key.indexOf(shared_folder) > -1)) {
        //Shared must be written into all areas
        result  :+= (jsType, "admin", ApplicationBuild.buildMode)
        return result :+ (jsType, "site", ApplicationBuild.buildMode)
      }
    } else {
      val tempPath = path.substring(path.indexOf('\\') + 1)
      key = tempPath.substring(0, tempPath.indexOf('\\'))
    }
    result :+ (jsType, key, ApplicationBuild.buildMode)
    
  }

  def isModeFile(name: String): Boolean = {
    var result = false;
    if (((ApplicationBuild.buildMode == "dev") && (name.indexOf(".min") == -1))
      || ((ApplicationBuild.buildMode == "prod") && (name.indexOf(".min") > -1)))
      result = true;
    result;
  }
}
