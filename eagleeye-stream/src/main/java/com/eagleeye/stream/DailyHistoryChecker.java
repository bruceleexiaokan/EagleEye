package com.eagleeye.stream;

import com.eagleeye.event.BreakOutEvent;
import com.eagleeye.metrics.AggregatedStockMetrics;

public class DailyHistoryChecker {

  private final StockMetricsHistory history;
  private BreakOutEvent event;

  private static final int MAX_DAYS = 14;

  public DailyHistoryChecker(StockMetricsHistory history) {
    this.history = history;
  }

  public BreakOutEvent breakOutCheck() {
    BreakOutEvent result = null;
    int latestLowInDays = -1;
    int latestHighInDays = -1;
    double lowestPrice = -1;
    double highestPrice = -1;
    AggregatedStockMetrics now = history.getMetrics(0);
    int i = 1;
    for (; i < MAX_DAYS - 1; ++i) {
      AggregatedStockMetrics m = history.getMetrics(i);
      if (null == m) {
        break;
      }
      if (-1 == lowestPrice) {
        latestLowInDays = i;
        latestHighInDays = i;
        lowestPrice = m.getAdjustedClosePrice();
        highestPrice = m.getAdjustedClosePrice();
      } else if (m.getAdjustedClosePrice() < lowestPrice) {
        latestLowInDays = i;
        lowestPrice = m.getAdjustedClosePrice();
      } else if (m.getAdjustedClosePrice() > highestPrice) {
        latestHighInDays = i;
        highestPrice = m.getAdjustedClosePrice();
      }
    }
    if (latestLowInDays > latestHighInDays && latestLowInDays != i - 1) {
      if (now.getAdjustedClosePrice() >= highestPrice && now.getLowestPrice() < highestPrice) {
        if (history.getMetrics(latestLowInDays).getVolume() < now.getVolume()) {
          if (event == null) {
            event = new BreakOutEvent(history.getMetrics(latestLowInDays), history.getMetrics(latestHighInDays));
            result = event;
          }
        }
      }
    }
    if (event != null) {
      if (now.getAdjustedClosePrice() < event.getNow().getLowestPrice()) {
        event = null;
      } else {
        event.setNow(now);
      }
    }
    return result;
  }
}
