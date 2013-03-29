import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Some basic file operation methods to write and read content from file system. This methods could maybe replaced with commons-io.
 *
 * @author Sebastian Sachtleben
 */
object FileUtils {

  // Write content to file with encoding
  def writeFile(file: File, content: String, encoding: String) : File = {
    val out = new PrintWriter(file, encoding)
    try {
      out.print(content)
    } finally {
      out.close
    }
    file
  }

  // We create our own fileToString method due using getLines() on scala.io.Source discards what characters were used for 
  // line terminators (\n, \r, \r\n, etc.).
  def fileToString(file: File, encoding: String) = {
    val inStream = new FileInputStream(file)
    val outStream = new ByteArrayOutputStream
    try {
      var reading = true
      while (reading) {
        inStream.read() match {
          case -1 => reading = false
          case c => outStream.write(c)
        }
      }
      outStream.flush()
    } finally {
      inStream.close()
    }
    new String(outStream.toByteArray(), encoding)
  }
  
  
  def readProperties(relativePath: String): java.util.Properties = {
    println(new File("").getAbsolutePath())
    val prop = new java.util.Properties()
    try {
      val in = new java.io.FileInputStream(relativePath)
      prop.load(in)
      in.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
    prop
  }
}