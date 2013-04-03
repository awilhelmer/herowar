import sbt._
import Keys._
import PlayKeys._
import PlayExceptions._
import play.api.PlayException

/**
 * CustomAssetsCompiler handles assets which will not be covered by the custom build in PlayAssetsCompiler.
 *
 * @author Sebastian Sachtleben
 */
trait CustomAssetsCompiler {

  val handlebarsSettings = SettingKey[Seq[String]]("handlebars-settings")
  val handlebarsEntryPoints = SettingKey[PathFinder]("handlebars-entry-points")

  def AdvancedAssetsCompiler(
    name: String,
    baseFolder: String,
    level: Int,
    watch: File => PathFinder,
    filesSetting: sbt.SettingKey[PathFinder],
    naming: (String, Boolean) => String,
    compile: (File, Seq[String]) => (String, Option[String], Seq[File]),
    optionsSettings: sbt.SettingKey[Seq[String]]) = {
    (state, sourceDirectory in Compile, resourceManaged in Compile, cacheDirectory, optionsSettings, filesSetting, classDirectory in Compile) map {
      (state, src, resources, cache, options, files, classDirectory) =>

        //state.log.info("AssetsCompiler: " + name)

        import java.io._

        val cacheFile = cache / name
        val currentInfos = watch(src).get.map(f => f -> FileInfo.lastModified(f)).toMap
        val (previousRelation, previousInfo) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)

        val concatFiles = false
        val concatPath = ""

        if (previousInfo != currentInfos) {

          // Delete previous generated files
          previousRelation._2s.foreach(IO.delete)

          val generated = (files x relativeTo(Seq(src / "assets"))).flatMap {
            case (sourceFile, name) => {
              if (baseFolder ne null) {
                val sourcePath = sourceFile.getAbsolutePath
                val relativePath = sourcePath.substring(sourcePath.lastIndexOf(baseFolder) + baseFolder.length() + 1)
                var foundLevel = relativePath.count(_ == '\\')
                if (foundLevel >= level) {
                  val parentPath = sourcePath.substring(0, sourcePath.lastIndexOf('\\'))
                  // state.log.info("Concat this folder: " + parentPath)
                  // concatFolders += new File(parentPath)
                }
                // state.log.info(relativePath + " - found level: " + foundLevel)
              }
              val (debug, min, dependencies) = compile(sourceFile, options)
              val out = new File(resources, "public/" + naming(name, false))
              val outMin = new File(resources, "public/" + naming(name, true))
              IO.write(out, debug)
              (dependencies ++ Seq(sourceFile)).toSet[File].map(_ -> out) ++ min.map { minified =>
                val outMin = new File(resources, "public/" + naming(name, true))
                IO.write(outMin, minified)
                (dependencies ++ Seq(sourceFile)).map(_ -> outMin)
              }.getOrElse(Nil)
            }
          }

          Sync.writeInfo(
            cacheFile,
            Relation.empty[File, File] ++ generated,
            currentInfos)(FileInfo.lastModified.format)

          generated.map(_._2).distinct.toList
        } else {

          // Return previously generated files
          previousRelation._2s.toSeq

        }

    }
  }

  def HandlebarsCompiler(handlebars: String) = {
    val compiler = new HandlebarsCompiler(handlebars);
    AdvancedAssetsCompiler("handlebars", "templates", 1, (_ ** "*.tmpl"), handlebarsEntryPoints,
      { (name, min) => "javascripts/" + name + (if (min) ".min.js" else ".js") },
      { (file, options) =>
         val (jsSource, dependencies) = compiler.compileDir(file, options)
        (jsSource, None, dependencies)
      }, handlebarsSettings)
  }

}