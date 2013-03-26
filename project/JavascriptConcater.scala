import java.io._

class JavascriptConcater {

	/**
   	 * find a file with the given name in the current directory or any subdirectory
  	 */
  	private def findFile(name: String): Option[File] = {
    	def findIn(dir: File): Option[File] = {
      		for (file <- dir.listFiles) {
        		if (file.isDirectory) {
          			findIn(file) match {
            			case Some(file) => return Some(file)
            			case None => // keep trying
          			}
        		} else if (file.getName == name) {
          			return Some(file)
        		}
      		}
      		None
    	}
    	findIn(new File("."))
  	}
  	
  	def compileDir(root: File, options: Seq[String]): (String, Seq[File]) = {
  		val dependencies = Seq.newBuilder[File]
  		val output = new StringBuilder
    	output ++= "Test"
    	(output.toString, dependencies.result)
  	}

}