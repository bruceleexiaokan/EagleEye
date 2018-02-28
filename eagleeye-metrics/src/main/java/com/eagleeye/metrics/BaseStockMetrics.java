package com.eagleeye.metrics;

import com.eagleeye.metrics.StockMetrics;

/**
 * Base stock metrics implemenation
 *
 */
public class BaseStockMetrics implements StockMetrics {

  protected final long startTimestamp;
  protected final long endTimestamp;
  protected Double openPrice;
  protected Double highestPrice;
  protected Double lowestPrice;
  protected Double closePrice;
  protected Double adjustedClosePrice;
  protected Long volume;
  protected Long asks;
  protected Long bids;

  public BaseStockMetrics(long startTimestamp, long endTimestamp) {
    this.startTimestamp = Math.min(startTimestamp, endTimestamp);
    this.endTimestamp = Math.max(startTimestamp, endTimestamp);
  }

  @Override
  public long getStartTimestamp() {
    return startTimestamp;
  }

  @Override
  public long getEndTimestamp() {
    return endTimestamp;
  }

  @Override
  public Double getOpenPrice() {
    return openPrice;
  }

  @Override
  public Double getHighestPrice() {
    return highestPrice;
  }

  @Override
  public Double getLowestPrice() {
    return lowestPrice;
  }

  @Override
  public Double getClosePrice() {
    return closePrice;
  }

  @Override
  public Double getAdjustedClosePrice() {
    return adjustedClosePrice;
  }

  @Override
  public Long getVolume() {
    return volume;
  }

  @Override
  public Long getAsks() {
    return asks;
  }

  @Override
  public Long getBids() {
    return bids;
  }

  public void setOpenPrice(Double openPrice) {
    this.openPrice = openPrice;
  }

  public void setHighestPrice(Double highestPrice) {
    this.highestPrice = highestPrice;
  }

  public void setLowestPrice(Double lowestPrice) {
    this.lowestPrice = lowestPrice;
  }

  public void setClosePrice(Double closePrice) {
    this.closePrice = closePrice;
  }

  public void setAdjustedClosePrice(Double adjustedClosePrice) {
    this.adjustedClosePrice = adjustedClosePrice;
  }

  public void setVolume(Long volume) {
    this.volume = volume;
  }

  public void setAsks(Long asks) {
    this.asks = asks;
  }

  public void setBids(Long bids) {
    this.bids = bids;
  }

}
