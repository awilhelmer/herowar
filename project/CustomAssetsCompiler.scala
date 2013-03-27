package sbt

import Keys._
import PlayKeys._
import PlayExceptions._
import play.api.PlayException

// ----- Assets
trait CustomAssetsCompiler {

	val coffeescriptSettings = SettingKey[Seq[String]]("coffeescript-settings")
	val coffeescriptEntryPoints = SettingKey[PathFinder]("coffeescript-entry-points")
	val handlebarsSettings = SettingKey[Seq[String]]("handlebars-settings")
	val handlebarsEntryPoints = SettingKey[PathFinder]("handlebars-entry-points")
	
	val concatFolders = SettingKey[Seq[File]]("concat-folders")
	val concat = TaskKey[Seq[File]]("concat")
	val concatTask = (state, concatFolders) map { (state, folders) =>
		state.log.info("Concat Task runs now !!!!")
		folders.map(f => println(f.getName()))
		Seq.empty[File]
	}

	def AdvancedAssetsCompiler(
		name: String,
		baseFolder: String,
		level: Int,
		watch: File => PathFinder, 
		filesSetting: sbt.SettingKey[PathFinder],
		naming: (String, Boolean) => String,
		compile: (File, Seq[String]) => (String, Option[String], Seq[File]),
		optionsSettings: sbt.SettingKey[Seq[String]]
	) = {
		(state, sourceDirectory in Compile, resourceManaged in Compile, cacheDirectory, optionsSettings, filesSetting, classDirectory in Compile) map { 
		(state, src, resources, cache, options, files, classDirectory) =>                                                                                                                                                                 
	
			state.log.info("AssetsCompiler: " + name)
		
			import java.io._
	      
			val cacheFile = cache / name    
	    	val currentInfos = watch(src).get.map(f => f -> FileInfo.lastModified(f)).toMap
	    	val (previousRelation, previousInfo) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)  
	    	
	    	val concatFiles = false
	    	val concatPath = ""                                                                                                                                                                                                                        
	
	    	if (previousInfo != currentInfos) {                                                                                                                                                                                                                                                                                    
	
	        	// Delete previous generated files
	        	previousRelation._2s.foreach(IO.delete)                                                                                                                                                                                                                                                                              
	
	        	val generated = (files x relativeTo(Seq(src / "assets" ))).flatMap {                                                                                                                                                                                                                                                  
	          		case (sourceFile, name) => {
	          			if (baseFolder ne null) {
	          				val sourcePath = sourceFile.getAbsolutePath
	          				val relativePath = sourcePath.substring(sourcePath.lastIndexOf(baseFolder) + baseFolder.length() + 1)
	          				var foundLevel = relativePath.count(_ == '\\')	     
	          				if (foundLevel >= level) {
	          					val parentPath = sourcePath.substring(0, sourcePath.lastIndexOf('\\'))
	          					state.log.info("Concat this folder: " + parentPath)
	          					concatFolders += new File(parentPath)
	          					(concatFolders) map { (folders) =>
									
								}
	          				}
//	          				state.log.info(relativePath + " - found level: " + foundLevel)
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
	            	currentInfos
	        	)(FileInfo.lastModified.format)                                                                                                                                                                                                                                                                        
	
	        	generated.map(_._2).distinct.toList
	      	} else {
	
	        	// Return previously generated files
	        	previousRelation._2s.toSeq
	      
			}
	
		}
	}

	def CustomCoffeescriptCompiler() = {
		AdvancedAssetsCompiler("coffeescript", "javascripts", 1, (_ ** "*.coffee"), coffeescriptEntryPoints,
        	{ (name, min) => name.replace(".coffee", if (min) ".min.js" else ".js") },
        	{ (coffeeFile, options) =>
        		import scala.util.control.Exception._
			    val jsSource = play.core.coffeescript.CoffeescriptCompiler.compile(coffeeFile, options)
			    val minified = catching(classOf[CompilationException]).opt(play.core.jscompile.JavascriptCompiler.minify(jsSource, Some(coffeeFile.getName())))
			    (jsSource, minified, Seq(coffeeFile))
        	}, coffeescriptSettings
      	)
	}
	
	def CustomHandlebarsCompiler() = {
		AdvancedAssetsCompiler("handlebars",	"templates", 0,	(_ ** "*.tmpl"), handlebarsEntryPoints,
        	{ (name, min) => "javascripts/" + name + ".pre" + (if (min) ".min.js" else ".js") },
        	{ (file, options) =>
        		//println(file)
        		val dependencies = Seq.newBuilder[File]
          		("Test", None, dependencies.result)
        	}, handlebarsSettings
      	)
	}
		
}