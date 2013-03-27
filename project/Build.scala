import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build with CustomAssetsCompiler {

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
  		coffeescriptEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.coffee"),
      	coffeescriptSettings := Seq.empty[String],
      	
      	handlebarsEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets"  ** "*.tmpl"),
      	handlebarsSettings := Seq.empty[String],
      	
      	// Override resource generators and add custom compiler
      	resourceGenerators in Compile <<= LessCompiler(Seq(_)),
      	resourceGenerators in Compile <+= CustomCoffeescriptCompiler,
      	resourceGenerators in Compile <+= CustomHandlebarsCompiler,
      	
      	// Start custom concat task after resources task
      	concatFolders := Seq.empty[File],
 		concat <<= concatTask,
    	resources in Compile <<= (resources in Compile).dependsOn(concat)
  	)

}
