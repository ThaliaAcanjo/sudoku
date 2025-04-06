package br.com.dio.service;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifierService {

    
    private Map<EventEnum, List<EventListener>> listener = new HashMap<>(){{
        put(CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscriber(final EventEnum eventType, final EventListener eventListener) {
        var selectedListeners = listener.get(eventType);
        selectedListeners.add(eventListener);
    }

    public void notify(final EventEnum eventType) {
        var selectedListeners = listener.get(eventType);
        selectedListeners.forEach(listener -> listener.update(eventType));
    }
}
