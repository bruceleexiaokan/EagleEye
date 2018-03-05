package com.eagleeye.stream;

import com.eagleeye.metrics.StockMetrics;

/**
 * Stock metrics stream
 * 
 * @author xinzli
 *
 */
public interface StockMetricsStream {

  /**
   * Stock metrics in stream way
   * 
   * @param metrics
   */
  void onMetrics(StockMetrics metrics);
}
