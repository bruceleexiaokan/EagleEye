package com.eagleeye.event;

import java.util.Date;

import com.eagleeye.metrics.AggregatedStockMetrics;
import com.eagleeye.stream.MetricConstants;

public class BreakOutEvent implements TriggeredEvent {

  private final AggregatedStockMetrics aPoint;
  private final AggregatedStockMetrics bPoint;
  private AggregatedStockMetrics now;

  public BreakOutEvent(AggregatedStockMetrics aPoint, AggregatedStockMetrics bPoint) {
    this.aPoint = aPoint;
    this.bPoint = bPoint;
    now = bPoint;
  }

  @Override
  public String getType() {
    return "BO";
  }

  public AggregatedStockMetrics getAPoint() {
    return aPoint;
  }

  public AggregatedStockMetrics getBPoint() {
    return bPoint;
  }

  public int getABDurationInDays() {
    return (int) ((bPoint.getStartTimestamp() - aPoint.getStartTimestamp()) / MetricConstants.MS_IN_ONE_DAY);
  }

  public double getABPriceDifference() {
    return bPoint.getAdjustedClosePrice() - aPoint.getAdjustedClosePrice();
  }

  public double getABPriceDifferenceRatio() {
    return Math.round(getABPriceDifference() * 10000 / bPoint.getAdjustedClosePrice()) / 100.;
  }

  public AggregatedStockMetrics getNow() {
    return now;
  }

  public void setNow(AggregatedStockMetrics now) {
    this.now = now;
  }


  public int getCDDurationInDays() {
    return (int) ((now.getStartTimestamp() - bPoint.getStartTimestamp()) / MetricConstants.MS_IN_ONE_DAY);
  }

  public double getCDPriceDifference() {
    return now.getAdjustedClosePrice() - bPoint.getAdjustedClosePrice();
  }

  public double getCDPriceDifferenceRatio() {
    return Math.round(getCDPriceDifference() * 10000 / bPoint.getAdjustedClosePrice()) / 100.;
  }

  public int getYear() {
    return new Date(aPoint.getStartTimestamp()).getYear() + 1900;
  }

}
