package com.herowar.dao;

import java.util.List;
import java.util.Map;

import com.ssachtleben.play.plugin.event.EventBinding;
import com.ssachtleben.play.plugin.event.EventBus;
import com.ssachtleben.play.plugin.event.Events;

public class EventsDAO {

	public static Map<String, List<EventBinding>> subscribers() {
		return ((EventBus) Events.instance()).getSubscribers();
	}

}
