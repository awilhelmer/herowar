import sbt.SettingKey
import scala.util.Random
import java.security.SecureRandom

/**
 * CacheNumber creates a random number for each build.
 *
 * @author Sebastian Sachtleben
 */
trait CacheNumber {
  
  lazy val cacheNumber = SettingKey[String]("cache-number")
  lazy val random = new Random(new SecureRandom())
  
  def generateCacheNumber(): String = {
    val table = "1234567890"
    Stream.continually(random.nextInt(table.size)).map(table).take(10).mkString
  }
  
}