package com.eagleeye.event;

import java.util.List;

/**
 * Event handler, only handle one type of event
 */
public interface EventHandler {

  /**
   * @return event type that the handler and handle
   */
  String getType();

  /**
   * Handle the input event
   * 
   * @param event input event to be handled
   * @return new triggered events generated during handling
   */
  List<TriggeredEvent> handle(TriggeredEvent event);

}
