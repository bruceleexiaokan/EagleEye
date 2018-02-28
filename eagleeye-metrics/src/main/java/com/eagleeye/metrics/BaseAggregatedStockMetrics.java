package com.eagleeye.metrics;

import com.eagleeye.metrics.AggregatedStockMetrics;
import com.eagleeye.metrics.StockMetrics;

/**
 * Implementation of aggregated stock metrics
 *
 */
public class BaseAggregatedStockMetrics extends BaseStockMetrics implements AggregatedStockMetrics {

  protected Double highestOpenPrice;
  protected Double lowestOpenPrice;
  protected Double highestClosePrice;
  protected Double lowestClosePrice;
  protected Double highestAdjustedClosePrice;
  protected Double lowestAdjustedClosePrice;

  public BaseAggregatedStockMetrics(long startTimestamp, long endTimestamp) {
    super(startTimestamp, endTimestamp);
  }

  @Override
  public boolean canAggregate(StockMetrics metrics) {
    return this.getStartTimestamp() <= metrics.getStartTimestamp() && this.getEndTimestamp() >= metrics.getEndTimestamp();
  }

  @Override
  public void aggregate(StockMetrics metrics) {
    if (canAggregate(metrics)) {
      if (metrics instanceof AggregatedStockMetrics) {
        doAggregate1((AggregatedStockMetrics) metrics);
      } else {
        doAggregate(metrics);
      }
    }
  }

  @Override
  public Double getHighestOpenPrice() {
    return highestOpenPrice;
  }

  @Override
  public Double getLowestOpenPrice() {
    return lowestOpenPrice;
  }

  @Override
  public Double getHighestClosePrice() {
    return highestClosePrice;
  }

  @Override
  public Double getLowestClosePrice() {
    return lowestClosePrice;
  }

  @Override
  public Double getHighestAdjustedClosePrice() {
    return highestAdjustedClosePrice;
  }

  @Override
  public Double getLowestAdjustedClosePrice() {
    return lowestAdjustedClosePrice;
  }

  public void setOpenPrice(Double openPrice) {
    throw new UnsupportedOperationException("setOpenPrice is not supported");
  }

  public void setClosePrice(Double closePrice) {
    throw new UnsupportedOperationException("setClosePrice is not supported");
  }

  public void setAdjustedClosePrice(Double adjustedClosePrice) {
    throw new UnsupportedOperationException("setAdjustedClosePrice is not supported");
  }

  private void doAggregate(StockMetrics metrics) {
    if (null != metrics.getOpenPrice()) {
      this.highestOpenPrice = null == this.highestOpenPrice || this.highestOpenPrice < metrics.getOpenPrice() ? metrics.getOpenPrice()
          : this.highestOpenPrice;
      this.lowestOpenPrice = null == this.lowestOpenPrice || this.lowestOpenPrice > metrics.getOpenPrice() ? metrics.getOpenPrice()
          : this.lowestOpenPrice;
      this.openPrice = metrics.getOpenPrice();
    }
    if (null != metrics.getHighestPrice()) {
      this.highestPrice = null == this.highestPrice || this.highestPrice < metrics.getHighestPrice() ? metrics.getHighestPrice()
          : this.highestPrice;
    }
    if (null != metrics.getLowestPrice()) {
      this.lowestPrice = null == this.lowestPrice || this.lowestPrice > metrics.getLowestPrice() ? metrics.getLowestPrice()
          : this.lowestPrice;
    }
    if (null != metrics.getClosePrice()) {
      this.highestClosePrice = null == this.highestClosePrice || this.highestClosePrice < metrics.getClosePrice() ? metrics.getClosePrice()
          : this.highestClosePrice;
      this.lowestClosePrice = null == this.lowestClosePrice || this.lowestClosePrice > metrics.getClosePrice() ? metrics.getClosePrice()
          : this.lowestClosePrice;
      this.closePrice = metrics.getClosePrice();
    }
    if (null != metrics.getAdjustedClosePrice()) {
      this.highestAdjustedClosePrice = null == this.highestAdjustedClosePrice || this.highestAdjustedClosePrice < metrics
          .getAdjustedClosePrice() ? metrics.getAdjustedClosePrice() : this.highestAdjustedClosePrice;
      this.lowestAdjustedClosePrice = null == this.lowestAdjustedClosePrice || this.lowestAdjustedClosePrice > metrics
          .getAdjustedClosePrice() ? metrics.getAdjustedClosePrice() : this.lowestAdjustedClosePrice;
      this.adjustedClosePrice = metrics.getAdjustedClosePrice();
    }
    if (null != metrics.getVolume()) {
      this.volume = null == this.volume ? metrics.getVolume() : this.volume + metrics.getVolume();
    }
    if (null != metrics.getAsks()) {
      this.asks = metrics.getAsks();
    }
    if (null != metrics.getBids()) {
      this.bids = metrics.getBids();
    }
  }

  private void doAggregate1(AggregatedStockMetrics metrics) {
    this.openPrice = metrics.getOpenPrice();
    if (null != metrics.getHighestOpenPrice()) {
      this.highestOpenPrice = null == this.highestOpenPrice || this.highestOpenPrice < metrics.getHighestOpenPrice() ? metrics
          .getHighestOpenPrice() : this.highestOpenPrice;
    }
    if (null != metrics.getLowestOpenPrice()) {
      this.lowestOpenPrice = null == this.lowestOpenPrice || this.lowestOpenPrice > metrics.getLowestOpenPrice() ? metrics
          .getLowestOpenPrice() : this.lowestOpenPrice;
    }
    if (null != metrics.getHighestPrice()) {
      this.highestPrice = null == this.highestPrice || this.highestPrice < metrics.getHighestPrice() ? metrics.getHighestPrice()
          : this.highestPrice;
    }
    if (null != metrics.getLowestPrice()) {
      this.lowestPrice = null == this.lowestPrice || this.lowestPrice > metrics.getLowestPrice() ? metrics.getLowestPrice()
          : this.lowestPrice;
    }
    this.closePrice = metrics.getClosePrice();
    if (null != metrics.getHighestClosePrice()) {
      this.highestClosePrice = null == this.highestClosePrice || this.highestClosePrice < metrics.getHighestClosePrice() ? metrics
          .getHighestClosePrice() : this.highestClosePrice;
    }
    if (null != metrics.getLowestClosePrice()) {
      this.lowestClosePrice = null == this.lowestClosePrice || this.lowestClosePrice > metrics.getLowestClosePrice() ? metrics
          .getLowestClosePrice() : this.lowestClosePrice;
    }
    this.adjustedClosePrice = metrics.getAdjustedClosePrice();
    if (null != metrics.getHighestAdjustedClosePrice()) {
      this.highestAdjustedClosePrice = null == this.highestAdjustedClosePrice || this.highestAdjustedClosePrice < metrics
          .getHighestAdjustedClosePrice() ? metrics.getHighestAdjustedClosePrice() : this.highestAdjustedClosePrice;
    }
    if (null != metrics.getLowestAdjustedClosePrice()) {
      this.lowestAdjustedClosePrice = null == this.lowestAdjustedClosePrice || this.lowestAdjustedClosePrice > metrics
          .getLowestAdjustedClosePrice() ? metrics.getLowestAdjustedClosePrice() : this.lowestAdjustedClosePrice;
    }
    if (null != metrics.getVolume()) {
      this.volume = null == this.volume ? metrics.getVolume() : this.volume + metrics.getVolume();
    }
    if (null != metrics.getAsks()) {
      this.asks = metrics.getAsks();
    }
    if (null != metrics.getBids()) {
      this.bids = metrics.getBids();
    }

  }

}
