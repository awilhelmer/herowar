package game.processor.meta;

import java.util.List;

public interface IProcessorObjetcs<T> extends IProcessor {
  public int getObjCount();

  public List<T> getObjetcs(int size);

  public void add(T obj);

  public void addObjects(List<T> list);

}