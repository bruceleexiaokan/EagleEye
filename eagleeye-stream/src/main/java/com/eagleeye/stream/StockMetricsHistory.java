package com.eagleeye.stream;

import java.util.Calendar;

import com.eagleeye.metrics.AggregatedStockMetrics;
import com.eagleeye.metrics.BaseAggregatedStockMetrics;
import com.eagleeye.metrics.StockMetrics;

public class StockMetricsHistory implements StockMetricsStream {

  private final StreamType type;
  private final AggregatedStockMetrics[] slots;
  private int currentSlot = 0;

  public StockMetricsHistory(StreamType type, int slotSize) {
    this.type = type;
    this.slots = new AggregatedStockMetrics[slotSize];
  }

  @Override
  public void onMetrics(StockMetrics metrics) {
    if (null != this.slots[currentSlot] && this.slots[currentSlot].getEndTimestamp() < metrics.getStartTimestamp()) {
      currentSlot = (currentSlot + 1) % slots.length;
      this.slots[currentSlot] = null;
    }
    if (null == this.slots[currentSlot]) {
      newSlot(metrics.getStartTimestamp());
    }
    this.slots[currentSlot].aggregate(metrics);
  }

  public StreamType getStreamType() {
    return type;
  }

  public AggregatedStockMetrics getMetrics(int previousDays) {
    int n = previousDays;
    int slot = currentSlot;
    while (n > 0) {
      slot = (slot + slots.length - 1) % slots.length;
      --n;
    }
    return this.slots[slot];
  }

  private void newSlot(long startTimestamp) {
    long startTime;
    long endTime;
    switch (type) {
      case HOURLY: {
        startTime = (startTimestamp / MetricConstants.MS_IN_ONE_HOUR) * MetricConstants.MS_IN_ONE_HOUR;
        endTime = startTime + MetricConstants.MS_IN_ONE_HOUR - 1;
        break;
      }
      case DAILY: {
        startTime = startTimestamp;
        endTime = startTime + MetricConstants.MS_IN_ONE_DAY - 1;
        // startTime = (startTimestamp / MetricConstants.MS_IN_ONE_DAY) *
        // MetricConstants.MS_IN_ONE_DAY;
        // endTime = startTime + MetricConstants.MS_IN_ONE_DAY - 1;
        break;
      }
      case WEEKLY: {
        Calendar calendar = Calendar.getInstance(MetricConstants.NEW_YORK_TIME_ZONE);
        startTime = (startTimestamp / MetricConstants.MS_IN_ONE_DAY) * MetricConstants.MS_IN_ONE_DAY;
        // FIXME may need +5 hours
        calendar.setTimeInMillis(startTime);
        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
          calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }
        // FIXME may need -5 hours
        startTime = calendar.getTimeInMillis();
        endTime = startTime + (7 * MetricConstants.MS_IN_ONE_DAY) - 1;
        break;
      }
      case MONTHLY: {
        Calendar calendar = Calendar.getInstance(MetricConstants.NEW_YORK_TIME_ZONE);
        startTime = (startTimestamp / MetricConstants.MS_IN_ONE_DAY) * MetricConstants.MS_IN_ONE_DAY;
        // FIXME may need +5 hours
        calendar.setTimeInMillis(startTime);
        while (calendar.get(Calendar.DATE) > 1) {
          calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of month.
        }
        // FIXME may need -5 hours
        startTime = calendar.getTimeInMillis();
        endTime = startTime + (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) * MetricConstants.MS_IN_ONE_DAY) - 1;
        break;
      }
      default: {
        throw new UnsupportedOperationException("Not supported");
      }
    }
    this.slots[currentSlot] = new BaseAggregatedStockMetrics(startTime, endTime);
  }

}
