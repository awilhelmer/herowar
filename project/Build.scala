import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val concaterOptions = SettingKey[Seq[String]]("play-concater-options")
    val concaterEntryPoints = SettingKey[PathFinder]("play-concater-entry-points")
    val handlebarsOptions = SettingKey[Seq[String]]("play-handlebars-options")
    val handlebarsEntryPoints = SettingKey[PathFinder]("play-handlebars-entry-points")

	def LogTask() =
	  	(concaterEntryPoints, sourceDirectory in Compile, cacheDirectory, streams) map { (files, src, cache, s) =>
	  		s.log.info("Stream")
	  		val watch = ("*.coffee")
	  		val cacheFile = cache / "Test"    
	  		// val currentInfos = watch(src).get.map(f => f -> FileInfo.lastModified(f)).toMap
	  		val (previousRelation, previousInfo) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)                                                                                                                                                                                                                          
	  		previousRelation._2s.toSeq
	  	}

	def JavascriptConcater() = {
		println("JavascriptConcater")
		val concater = new JavascriptConcater()
		MyAssetsCompiler("coffeescript",
        	(_ ** "*.coffee"),
        	coffeescriptEntryPoints,
        	{ (name, min) => "javascripts/" + name + ".pre" + (if (min) ".min.js" else ".js") },
        	{ (file, options) =>
        		println(file)
          		val (jsSource, dependencies) = concater.compileDir(file, options)
          		import scala.util.control.Exception._
          		val minified = catching(classOf[CompilationException])
            		.opt(play.core.jscompile.JavascriptCompiler.minify(jsSource, Some(file.getName())))
          		(jsSource, minified, dependencies)
        	},
        	concaterOptions
      	)
	}
	
	def HandlebarsCompiler() = {
		println("HandlebarsCompiler")
		MyAssetsCompiler("handlebars",
        	(_ ** "*.tmpl"),
        	handlebarsEntryPoints,
        	{ (name, min) => "javascripts/" + name + ".pre" + (if (min) ".min.js" else ".js") },
        	{ (file, options) =>
        		println(file)
        		val dependencies = Seq.newBuilder[File]
          		("Test", None, dependencies.result)
        	},
        	handlebarsOptions
      	)
	}
	
	// Name: name of the compiler
  // files: the function to find files to compile from the assets directory
  // naming: how to name the generated file from the original file and whether it should be minified or not
  // compile: compile the file and return the compiled sources, the minified source (if relevant) and the list of dependencies                                                                                                                                                                                               
  def MyAssetsCompiler(
    name: String,  
    watch: File => PathFinder, 
    filesSetting: sbt.SettingKey[PathFinder],
    naming: (String, Boolean) => String,
    compile: (File, Seq[String]) => (String, Option[String], Seq[File]),
    optionsSettings: sbt.SettingKey[Seq[String]]) = {
    
    println("MyAssetsCompiler")
    println(sourceDirectory in Compile)
    println(resourceManaged in Compile)
    println(cacheDirectory)
    println(optionsSettings)
    println(filesSetting)
    println(classDirectory in Compile)
    
    (streams, sourceDirectory in Compile, resourceManaged in Compile, cacheDirectory, optionsSettings, filesSetting, classDirectory in Compile) map { (s, src, resources, cache, options, files, classDirectory) =>                                                                                                                                                                 

	  s.log.info("MyAssetsCompiler2")

      import java.io._
      
      println("MyAssetsCompiler3")

      val cacheFile = cache / name    
      val currentInfos = watch(src).get.map(f => f -> FileInfo.lastModified(f)).toMap
      val (previousRelation, previousInfo) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)                                                                                                                                                                                                                          

      if (previousInfo != currentInfos) {                                                                                                                                                                                                                                                                                    

        // Delete previous generated files
        previousRelation._2s.foreach(IO.delete)                                                                                                                                                                                                                                                                              

        val generated = (files x relativeTo(Seq(src / "assets" ))).flatMap {                                                                                                                                                                                                                                                  
          case (sourceFile, name) => { 
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
            currentInfos
        )(FileInfo.lastModified.format)                                                                                                                                                                                                                                                                        

        generated.map(_._2).distinct.toList
      } else {

        // Return previously generated files
        previousRelation._2s.toSeq                                                                                                                                                                                                                                                                                           

      }
      
      }

    }: sbt.Project.Initialize[sbt.Task[Seq[java.io.File]]]  

	val appName         = "herowar"
	val appVersion      = "1.0-SNAPSHOT"

	val appDependencies = Seq(
    	// Add your project dependencies here,
    	javaCore,
    	javaJdbc,
    	javaEbean,
    	"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
  	)

  	val main = play.Project(appName, appVersion, appDependencies).settings(
  		concaterEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" / "javascripts"),
      	concaterOptions := Seq.empty[String],
      	
      	handlebarsEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" / "templates"),
      	handlebarsOptions := Seq.empty[String]
      	
      	// Override resource generators and add less and coffescript compiler
      	//resourceGenerators in Compile <<= LessCompiler(Seq(_)),
      	//resourceGenerators in Compile <+= JavascriptConcater,
      	//resourceGenerators in Compile <+= HandlebarsCompiler,
      	//resourceGenerators in Compile <+= LogTask
  	)
  	
  	println("Test")

}
