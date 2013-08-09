package com.herowar.json;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonDeserializer;

import com.herowar.util.JsonUtils;

import play.Logger;

public abstract class BaseDeserializer<T> extends JsonDeserializer<T> {
	private static final Logger.ALogger log = Logger.of(BaseSerializer.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T parseObject(JsonNode node, Class<?>... parseClasses) {
		T result = null;
		List<Class<?>> classes = Arrays.asList(parseClasses);
		try {
			result = (T) ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
			JsonUtils.parse(result, node, classes);
		} catch (Exception e) {
			log.error("", e);
		}
		return result;

	}
}
