import java.io._
import play.api._

class HandlebarsCompiler(handlebars: String) {

  import org.mozilla.javascript._
  import org.mozilla.javascript.tools.shell._
 
  import scala.collection.JavaConverters._
 
  import scalax.file._
  
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
  
  private lazy val compiler = {
    val ctx = Context.enter; ctx.setOptimizationLevel(-1)
    val global = new Global; global.init(ctx)
    val scope = ctx.initStandardObjects(global)

    // set up global objects that emulate a browser context
    ctx.evaluateString(scope,
      """
        // make window an alias for the global object
        var window = this,
            document = {
              createElement: function(type) {
                return {
                  firstChild: {}
                };
              },
              getElementById: function(id) { 
                return [];
              },
              getElementsByTagName: function(tagName) {
                return [];
              }
            },
            location = {
              protocol: 'file:', 
              hostname: 'localhost',
              href: 'http://localhost:80',
              port: '80'
            },
            console = {
              log: function() {},
              info: function() {},
              warn: function() {},
              error: function() {}
            }
        
        // make a dummy jquery object just to make ember happy
        var jQuery = function() { return jQuery; };
        jQuery.ready = function() { return jQuery; };
        jQuery.inArray = function() { return jQuery; };
        jQuery.jquery = "1.7.1";
        var $ = jQuery;
        
        // our precompile function uses Ember to do the precompilation,
        // then converts the compiled function to its string representation
        function precompile(string) {
          return Handlebars.precompile(string);
        }
      """,
      "browser.js",
      1, null)

    // load ember
    val handlebarsFile = findFile(handlebars).getOrElse(throw new Exception("handlebars: could not find " + handlebars))
    ctx.evaluateString(scope, Path(handlebarsFile).string, handlebars, 1, null)

    val precompileFunction = scope.get("precompile", scope).asInstanceOf[Function]

    Context.exit

    (source: File) => {
      val handlebarsCode = Path(source).string.replace("\r", "")
      Context.call(null, precompileFunction, scope, scope, Array(handlebarsCode)).asInstanceOf[String]
    }
  }

  def compileDir(file: File, options: Seq[String]): (String, Seq[File]) = {
    val dependencies = Seq.newBuilder[File]
    val jsSource = compile(file, options)
    (jsSource, dependencies.result)
  }

  private def compile(source: File, options: Seq[String]): String = {
    try {
      compiler(source)
    } catch {
      case e: Exception => 
        println("exception caught: " + e)
        exit(1)
    }
  }

}