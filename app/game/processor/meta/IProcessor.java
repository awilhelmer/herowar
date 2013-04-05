package game.processor.meta;


/**
 * @author Sebastian Sachtleben
 */

public interface IProcessor {

  public void process();
  
  public int getUpdateTimer();
  
  public void start();
  
  public void stop();
  
  public boolean isRunning();
  
  public void setRunning(boolean running);
  
}
