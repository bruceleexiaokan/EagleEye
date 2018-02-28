package com.eagleeye.metrics;

/**
 * Stock metrics data interface
 */
public interface StockMetrics {

  /**
   * @return get start timestamp of the metrics
   */
  long getStartTimestamp();

  /**
   * @return get end timestamp of the metrics
   */
  long getEndTimestamp();

  /**
   * @return open price
   */
  Double getOpenPrice();

  /**
   * @return highest price
   */
  Double getHighestPrice();

  /**
   * @return Lowest price
   */
  Double getLowestPrice();

  /**
   * @return close price
   */
  Double getClosePrice();

  /**
   * @return adjusted close price
   */
  Double getAdjustedClosePrice();

  /**
   * @return volume
   */
  Long getVolume();

  /**
   * @return asks
   */
  Long getAsks();

  /**
   * @return bids
   */
  Long getBids();

}
