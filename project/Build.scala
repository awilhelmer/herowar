import sbt._
import Keys._
import play.Project._
import sbtbuildinfo.Plugin._
import com.typesafe.sbteclipse.core.EclipsePlugin._

/**
 * Main application build definition for our playframework app.
 *
 * @author Sebastian Sachtleben
 */
object ApplicationBuild extends Build with CustomAssetsCompiler with JavascriptTransformer with JavascriptFilter with CacheNumber {

  ////////// VARIABLES //////////

  lazy val buildProperties = FileUtils.readProperties("project\\build.properties")
  val (appName, appVersion, handlebarsJS, buildMode) = (buildProperties.getProperty("appName"), buildProperties.getProperty("appVersion"), buildProperties.getProperty("handlebarsJS"), buildProperties.getProperty("buildMode"))

  ////////// DEPENDENCIES //////////

  val appSettings = Seq[Setting[_]](
    EclipseKeys.skipParents in ThisBuild := false,
    ebeanEnabled := false,
    resolvers += Resolver.url("ssachtleben repository (snapshots)", url("http://ssachtleben.github.io/play-plugins/repository/snapshots/"))(Resolver.ivyStylePatterns))

  val resourceSettings = buildInfoSettings ++ Seq[Setting[_]](

    // Configure build info scala file
    sourceGenerators in Compile <+= buildInfo,
    cacheNumber := generateCacheNumber,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, cacheNumber),
    buildInfoPackage := "info",

    // Configure handlebars compiler
    handlebarsEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.tmpl"),
    handlebarsSettings := Seq.empty[String],
    resourceGenerators in Compile <+= HandlebarsCompiler(handlebars = handlebarsJS),

    // Add javascript transformer method
    resources in Compile <<= (classDirectory in Compile, resources in Compile, cacheNumber) map transformResources,

    // Add javascript filter method
    copyResources in Compile <<= (copyResources in Compile, playCopyAssets, cacheNumber) map filterResources)

  ////////// DEPENDENCIES //////////

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa,
    "com.ssachtleben" %% "play-auth-plugin" % "0.1-SNAPSHOT",
    "commons-beanutils" % "commons-beanutils" % "1.8.3",
    "mysql" % "mysql-connector-java" % "5.1.24",
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final")

  ////////// PROJECTS //////////

  val main = play.Project(appName, appVersion, appDependencies, settings = Defaults.defaultSettings ++ appSettings ++ resourceSettings)

}
