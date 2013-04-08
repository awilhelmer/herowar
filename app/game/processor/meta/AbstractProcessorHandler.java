package game.processor.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import play.Logger;

/**
 * 
 * @author Alexander Wilhelmer
 *
 * @param <T>
 */
public abstract class AbstractProcessorHandler<T> extends AbstractProcessor implements IProcessor {

  private final static Logger.ALogger log = Logger.of(AbstractProcessorHandler.class);
  private List<IProcessorObjetcs<T>> processors = Collections.synchronizedList(new ArrayList<IProcessorObjetcs<T>>());
  private IProcessorObjetcs<T> actualProcessors;
  private Long objectIdGenerator = 0l;

  public AbstractProcessorHandler(String topic) {
    super(topic);
 

  }

  @Override
  public void process() {
    synchronized (processors) {
      if (actualProcessors == null) {
        actualProcessors = getNewProcessor();
        this.processors.add(actualProcessors);
        this.actualProcessors.start();
      }
      Iterator<IProcessorObjetcs<T>> processorIt = processors.iterator();
      while (processorIt.hasNext()) {
        IProcessorObjetcs<T> processor = processorIt.next();
        if (processor.getObjCount() > getMaxObjPerProcessor()) {
          int size = processor.getObjCount() - getMaxObjPerProcessor();
          List<T> list = processor.getObjetcs(size - 1);
          int sizeToAdd = getMaxObjPerProcessor() - this.actualProcessors.getObjCount();
          if (list.size() <= sizeToAdd) {
            this.actualProcessors.addObjects(list);
          } else {
            List<T> subList = list.subList(list.size() - sizeToAdd, list.size());
            this.actualProcessors.addObjects(subList);
            this.actualProcessors = getNewProcessor();
            this.processors.add(actualProcessors);
            this.actualProcessors.start();
            this.actualProcessors.addObjects(list);
          }
        } else if (processor.getObjCount() == 0) {
          if (processor != this.actualProcessors) {
            processor.stop();
            processorIt.remove();
          }
        }
      }
    }
  }


  public void add(T obj) {
    synchronized (processors) {
      if (actualProcessors.getObjCount() > getMaxObjPerProcessor()) {
        this.actualProcessors = getNewProcessor();
        this.processors.add(actualProcessors);
        this.actualProcessors.start();
      }
      log.info(String.format("Add Obj actual Processorsize <%s>", this.actualProcessors.getObjCount()));
      this.actualProcessors.add(obj);

    }
  }

  public synchronized Long getObjectIdGenerator() {
    return new Long(objectIdGenerator++);
  }

  @Override
  public void stop() {
    for (IProcessorObjetcs<T> processor : processors) {
      processor.stop();
    }
    processors.clear();
    super.stop();
  }

  public abstract IProcessorObjetcs<T> getNewProcessor();

  public abstract int getMaxObjPerProcessor();
}
