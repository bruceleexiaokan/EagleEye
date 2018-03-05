package com.eagleeye.stream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eagleeye.metrics.StockMetrics;

public class StockMetricsHistoryList implements StockMetricsStream {

  private final Map<StreamType, StockMetricsHistory> map = new HashMap<>();

  public StockMetricsHistoryList(List<StockMetricsHistory> list) {
    for (StockMetricsHistory history : list) {
      map.put(history.getStreamType(), history);
    }
  }

  @Override
  public void onMetrics(StockMetrics metrics) {
    for (StockMetricsHistory h : map.values()) {
      h.onMetrics(metrics);
    }
  }

}
