import sbt._
import Keys._
import play.Project._
import sbtbuildinfo.Plugin._

/**
 * Main application build definition for our playframework app.
 *
 * @author Sebastian Sachtleben
 */
object ApplicationBuild extends Build with CustomAssetsCompiler with JavascriptTransformer with JavascriptFilter with CacheNumber {

  ////////// VARIABLES //////////
  lazy val buildProperties = readProperties()
  val (appName, appVersion, handlebarsJS, buildMode) = (buildProperties.getProperty("appName"), buildProperties.getProperty("appVersion"), buildProperties.getProperty("handlebarsJS"), buildProperties.getProperty("buildMode"))

  ////////// DEPENDENCIES //////////

  val appSettings = Seq[Setting[_]](
    resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns))

  val resourceSettings = buildInfoSettings ++ Seq[Setting[_]](
    sourceGenerators in Compile <+= buildInfo,
    cacheNumber := generateCacheNumber,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, cacheNumber),
    buildInfoPackage := "info",
    handlebarsEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.tmpl"),
    handlebarsSettings := Seq.empty[String],
    resourceGenerators in Compile <+= HandlebarsCompiler(handlebars = handlebarsJS),
    resources in Compile <<= (classDirectory in Compile, resources in Compile, cacheNumber) map transformResources,
    copyResources in Compile <<= (copyResources in Compile, playCopyAssets, cacheNumber) map filterResources)

  ////////// DEPENDENCIES //////////

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaEbean,
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
    "com.feth" %% "play-authenticate" % "0.2.5-SNAPSHOT")

  val commonDependencies = Seq(
    "org.apache.commons" % "commons-lang3" % "3.1")

  ////////// PROJECTS //////////

  val common = play.Project(appName + "-common", appVersion, appDependencies ++ commonDependencies, path = file("modules/common"), settings = Defaults.defaultSettings ++ appSettings)
  val site = play.Project(appName + "-site", appVersion, appDependencies, path = file("modules/site"), settings = Defaults.defaultSettings ++ appSettings ++ resourceSettings).dependsOn(common)
  val main = play.Project(appName, appVersion, appDependencies, settings = Defaults.defaultSettings ++ appSettings).dependsOn(common, site).aggregate(common, site)

  def readProperties(): java.util.Properties = {
    println(new File("").getAbsolutePath())
    val prop = new java.util.Properties()
    try {
      val in = new java.io.FileInputStream("project\\build.properties")
      prop.load(in)
      in.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
    prop
  }

}
