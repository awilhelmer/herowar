import sbt._
import Keys._
import play.Project._

/**
 * Main application build definition for our playframework app.
 *
 * @author Sebastian Sachtleben
 */
object ApplicationBuild extends Build with CustomAssetsCompiler with JavascriptTransformer {

  val appName = "herowar"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final")

  val main = play.Project(appName, appVersion, appDependencies).settings(
    //coffeescriptOptions := Seq("bare"),

    //coffeescriptEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.coffee"),
    //coffeescriptSettings := Seq.empty[String],

    handlebarsEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.tmpl"),
    handlebarsSettings := Seq.empty[String],

    // Override resource generators and add custom compiler
    //resourceGenerators in Compile <<= LessCompiler(Seq(_)),
    //resourceGenerators in Compile <+= CustomCoffeescriptCompiler,
    resourceGenerators in Compile <+= CustomHandlebarsCompiler,

    // This will take the current sequence of resources and apply the transformResources method
    resources in Compile ~= transformResources)

}
