package game.processor.meta;

import game.processor.GameProcessor;

import org.bushe.swing.event.annotation.AnnotationProcessor;

/**
 * The AbstractPlugin provides methods used by any plugin.
 * 
 * @author Sebastian Sachtleben
 */
public class AbstractPlugin {

  protected GameProcessor processor;

  public AbstractPlugin(GameProcessor processor) {
    this.processor = processor;
  }

  public void load() {
    AnnotationProcessor.process(this);
  }

  public void unload() {
    AnnotationProcessor.unprocess(this);
  }

  public GameProcessor getProcessor() {
    return processor;
  }

  public String getTopicName() {
    return getProcessor().getTopicName();
  }
}
