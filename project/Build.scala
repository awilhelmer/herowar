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
    resolvers += Resolver.url("Objectify Play Repository (release)", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Repository (snapshot)", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns))

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
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
    "be.objectify" %% "deadbolt-java" % "2.1-SNAPSHOT",
    "com.feth" %% "play-authenticate" % "0.2.5-SNAPSHOT")

  val commonDependencies = Seq(
    "org.apache.commons" % "commons-lang3" % "3.1")

  ////////// PROJECTS //////////

  val common = play.Project(appName + "-common", appVersion, appDependencies ++ commonDependencies, path = file("modules/common"), settings = Defaults.defaultSettings ++ appSettings)
  val admin = play.Project(appName + "-admin", appVersion, appDependencies, path = file("modules/admin"), settings = Defaults.defaultSettings ++ appSettings ++ resourceSettings).dependsOn(common)
  val game = play.Project(appName + "-game", appVersion, appDependencies, path = file("modules/game"), settings = Defaults.defaultSettings ++ appSettings).dependsOn(common)
  val site = play.Project(appName + "-site", appVersion, appDependencies, path = file("modules/site"), settings = Defaults.defaultSettings ++ appSettings ++ resourceSettings).dependsOn(common)
  val main = play.Project(appName, appVersion, appDependencies, settings = Defaults.defaultSettings ++ appSettings).dependsOn(common, admin, game, site).aggregate(common, admin, game, site)

}
