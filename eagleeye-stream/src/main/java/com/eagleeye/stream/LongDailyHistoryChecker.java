package com.eagleeye.stream;

import com.eagleeye.event.BreakOutEvent;
import com.eagleeye.metrics.AggregatedStockMetrics;

public class LongDailyHistoryChecker {

  private final StockMetricsHistory history;
  private BreakOutEvent event;

  private static final int MAX_DAYS = 14;

  public LongDailyHistoryChecker(StockMetricsHistory history) {
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
    long longVolume = 0;
    long shortVolume = 0;
    for (; i < MAX_DAYS - 1; ++i) {
      AggregatedStockMetrics m = history.getMetrics(i);
      if (null == m) {
        break;
      }
      if (m.getOpenPrice() >= m.getAdjustedClosePrice()) {
        longVolume += m.getVolume();
      } else {
        shortVolume += m.getVolume();
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
    if (event == null) {
      // if (longVolume > shortVolume) {
      if (latestLowInDays > latestHighInDays && latestLowInDays != i - 1) {
        if (now.getAdjustedClosePrice() >= highestPrice && now.getLowestPrice() < highestPrice) {
          if (history.getMetrics(latestHighInDays).getVolume() < now.getVolume()) {
            BreakOutEvent event = new BreakOutEvent(true, history.getMetrics(latestLowInDays), history.getMetrics(latestHighInDays));
            this.event = event;
            if (shortVolume > 0) {
              event.setLongShortRatio((double) longVolume / (double) shortVolume);
            }
            result = event;
          }
        }
      }
      // }
    }
    if (event != null) {
      if (event.getCPoint() == null) {
        event.setCPoint(now);
        event.setDPoint(now);
      } else {
        if (event.isFinalizing()) {
          event.setDPoint(now);
          event = null;
        } else if (now.getOpenPrice() < event.getDPoint().getOpenPrice() && now.getClosePrice() < event.getDPoint().getClosePrice()) {
          // } else if (now.getAdjustedClosePrice() < event.getDPoint().getLowestPrice()
          // || now.getLowestPrice() <= event.getCPoint().getLowestPrice()) {
          event.setDPoint(now);
          event.setFinalizing(true);
        } else {
          event.setDPoint(now);
        }
      }
    }
    return result;
  }
}
