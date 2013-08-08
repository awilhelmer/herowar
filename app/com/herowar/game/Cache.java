package com.herowar.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Cache<K, V> {

	private ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<K, V>();

	public Map<K, V> cache() {
		return cache;
	}

}
