import scala.collection.mutable._

object FileCacheHandler {
   var unchangedModules = ListBuffer.empty[(String, String, String)]
   val filesChanged = Map[String,Long]()
}