package com.eagleeye.event;

public interface EventProcessor {

  void register(EventHandler handler);

  EventHandler unregister(String type);

  void process(TriggeredEvent event);

}
