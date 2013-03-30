import scala.collection.mutable._

object FileCacheHandler {
   var unchangedModules = List.empty[(String, String, String)]
   val filesChanged = Map[String,Long]()
}