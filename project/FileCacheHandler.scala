import scala.collection.mutable._

object FileCacheHandler {
   var unchangedModules = ListBuffer.empty[(String, String, String)]
   var writtenFiles = scala.collection.Seq.empty[java.io.File]
   val filesChanged = Map[String,Long]()
   
}