package com.herowar.game.processor.meta;

import java.util.List;

/**
 * @author Alexander Wilhelmer
 * 
 * @param <T>
 */
public interface IProcessorObjetcs<T> extends IProcessor {

	public int getObjCount();

	public List<T> getObjetcs(int size);

	public void add(T obj);

	public void addObjects(List<T> list);

}
