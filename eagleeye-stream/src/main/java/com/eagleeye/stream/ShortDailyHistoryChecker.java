package com.eagleeye.stream;

import com.eagleeye.event.BreakOutEvent;
import com.eagleeye.metrics.AggregatedStockMetrics;

public class ShortDailyHistoryChecker {

  private final StockMetricsHistory history;
  private BreakOutEvent event;

  private static final int MAX_DAYS = 14;

  public ShortDailyHistoryChecker(StockMetricsHistory history) {
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
        lowestPrice = m.getClosePrice();
        highestPrice = m.getClosePrice();
      } else if (m.getClosePrice() < lowestPrice) {
        latestLowInDays = i;
        lowestPrice = m.getClosePrice();
      } else if (m.getClosePrice() > highestPrice) {
        latestHighInDays = i;
        highestPrice = m.getClosePrice();
      }
    }
    if (event == null) {
      // if (longVolume < shortVolume) {
      if (latestHighInDays > latestLowInDays && latestHighInDays != i - 1) {
        if (now.getClosePrice() <= lowestPrice && now.getHighestPrice() > lowestPrice) {
          if (history.getMetrics(latestLowInDays).getVolume() < now.getVolume()) {
            BreakOutEvent event = new BreakOutEvent(false, history.getMetrics(latestHighInDays), history.getMetrics(latestLowInDays));
            this.event = event;
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
        } else if (now.getOpenPrice() > event.getDPoint().getOpenPrice() && now.getClosePrice() > event.getDPoint().getClosePrice()) {
          // } else if (now.getAdjustedClosePrice() > event.getDPoint().getHighestPrice()
          // || (now.getHighestPrice() >= event.getCPoint().getHighestPrice())) {
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
