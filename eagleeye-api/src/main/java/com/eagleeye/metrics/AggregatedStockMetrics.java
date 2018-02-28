package com.eagleeye.metrics;

/**
 * Aggregated stock metrics
 *
 */
public interface AggregatedStockMetrics extends StockMetrics {

  /**
   * @param metrics metrics to aggregate
   * @return true if it can aggregate, otherwise return false
   */
  boolean canAggregate(StockMetrics metrics);

  /**
   * Aggregate the metrics if possible
   * 
   * @param metrics metrics to aggregate
   */
  void aggregate(StockMetrics metrics);

  /**
   * @return highest open price
   */
  Double getHighestOpenPrice();

  /**
   * @return lowest open price
   */
  Double getLowestOpenPrice();

  /**
   * @return highest close price
   */
  Double getHighestClosePrice();

  /**
   * @return lowest close price
   */
  Double getLowestClosePrice();

  /**
   * @return highest adjusted close price
   */
  Double getHighestAdjustedClosePrice();

  /**
   * @return lowest adjusted close price
   */
  Double getLowestAdjustedClosePrice();

}
