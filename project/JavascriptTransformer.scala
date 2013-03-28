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
  // This takes the raw resources, which are the .css files and  the .js files from coffeescript and handlebars.  It separates
  //  the .js files from the .css files and transforms just the .js files.
  def transformResources(classFolder: Seq[sbt.File],original: Seq[File]): Seq[File] = {
    val (js, nonJs) = original.partition(_.getName.endsWith(".js"))
    nonJs ++ transformJs(classFolder,js)
  }

  // This takes the list of all .js files.  It should transform them into new files, such as by concatenating them and writing 
  // them to new files. The list of new files should be returned.
  def transformJs(classFolder:  Seq[sbt.File], jsFiles: Seq[File]): Seq[File] = {
   
    println(classFolder);
    
    var (loader, distPath, cutPath, content) = ("", "", "javascripts\\", Map[(String, String, String), String]())
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

        // Special case for loader.js file, save content to loaderMin variable
        case "loader.js" | "loader.min.js" => {
          if (isModeFile(relativePath)) {
            loader = fileContent;
          }
        }
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
          if (isModeFile(functionName)) {
            content.put(((jsType, key, ApplicationBuild.buildMode)), content.get((jsType, key, ApplicationBuild.buildMode)).getOrElse("") + mappedContent + "\n")
          }
        }
      }
    })
    if (loader == "") throw new Exception("Couldn't find loader in root javascript folder!")
    // Write content to file system
    writeCombinedFiles(distPath, content, loader)

    Seq.empty[File]
  }

  /**
   * Write combined files to output generated from Map[(String, String, String), String]].
   */
  def writeCombinedFiles(path: String, content: Map[(String, String, String), String], loader: String) = {
    for ((tuple, entries) <- content) {
      val fileName = path + tuple._1 + "_" + tuple._2 + ".js"
      println("Write file: " + fileName)
      var fileContent = ""
      if (fileName.indexOf("scripts_") > -1) {
        fileContent = loader;
      }
      fileContent += content(tuple);

      writeFile(new File(fileName), fileContent, "UTF-8")
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

  def isModeFile(name: String): Boolean = {
    var result = false;
    if (((ApplicationBuild.buildMode == "dev") && (name.indexOf(".min") == -1))
      || ((ApplicationBuild.buildMode == "prod") && (name.indexOf(".min") > -1)))
      result = true;
    result;
  }
}
