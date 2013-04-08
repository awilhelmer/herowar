package game.processor;

import game.processor.meta.IProcessor;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
/**
 * @author Alexander Wilhelmer
 */
public class ProcessorHandler {

  private final static Logger.ALogger log = Logger.of(ProcessorHandler.class);
  
  private List<IProcessor> processors = new ArrayList<IProcessor>();
  private boolean started = false;

  public ProcessorHandler(List<IProcessor> processors) {
    this.processors = processors;
  }
  
  public void start() {
    if (started) {
      throw new RuntimeException(this.toString() + " has already been started");
    }
    for (IProcessor proc : processors) {
      proc.start();
      log.debug(proc.toString() + " started");
    }
    started = true;
  }
  
  public void pause() {
    if (!started) {
      throw new RuntimeException(this.toString() + " is not started yet");
    }
    for (IProcessor proc : processors) {
      proc.setRunning(proc.isRunning() ? false : true);
      log.debug(proc.toString() + " paused");
    }
  }
  
  public void stop() {
    if (!started) {
      throw new RuntimeException(this.toString() + " has already been stopped");
    }
    for (IProcessor proc : processors) {
      proc.stop();
      log.debug(proc.toString() + " stopped");
    }
    started = false;
  }
  
  public List<IProcessor> getProcessors() {
    return processors;
  }

  public void setProcessors(List<IProcessor> processors) {
    this.processors = processors;
  }
  
  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }
  
}
