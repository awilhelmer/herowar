package game.processor.meta;

import game.processor.GameProcessor;

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

  public GameProcessor getProcessor() {
    return processor;
  }  
}
