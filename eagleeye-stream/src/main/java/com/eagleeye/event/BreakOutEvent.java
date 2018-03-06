package com.eagleeye.event;

import java.util.Date;

import com.eagleeye.metrics.AggregatedStockMetrics;
import com.eagleeye.stream.MetricConstants;

public class BreakOutEvent implements TriggeredEvent {

  private final boolean isLong;
  private final AggregatedStockMetrics aPoint;
  private final AggregatedStockMetrics bPoint;
  private AggregatedStockMetrics cPoint;
  private AggregatedStockMetrics dPoint;
  private double firstTradeDayRatio = -1.;
  private boolean isFinalizing = false;
  private double longShortRatio = -1;

  public BreakOutEvent(boolean isLong, AggregatedStockMetrics aPoint, AggregatedStockMetrics bPoint) {
    this.isLong = isLong;
    this.aPoint = aPoint;
    this.bPoint = bPoint;
    dPoint = bPoint;
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
    if (isLong) {
      return bPoint.getAdjustedClosePrice() - aPoint.getAdjustedClosePrice();
    } else {
      return aPoint.getAdjustedClosePrice() - bPoint.getAdjustedClosePrice();
    }
  }

  public double getABPriceDifferenceRatio() {
    return Math.round(getABPriceDifference() * 10000 / bPoint.getAdjustedClosePrice()) / 100.;
  }

  public AggregatedStockMetrics getCPoint() {
    return cPoint;
  }

  public void setCPoint(AggregatedStockMetrics cPoint) {
    this.cPoint = cPoint;
  }

  public AggregatedStockMetrics getDPoint() {
    return dPoint;
  }

  public void setDPoint(AggregatedStockMetrics dPoint) {
    this.dPoint = dPoint;
  }


  public int getCDDurationInDays() {
    return (int) ((dPoint.getStartTimestamp() - cPoint.getStartTimestamp()) / MetricConstants.MS_IN_ONE_DAY);
  }

  public double getCDPriceDifference() {
    if (isLong) {
      return dPoint.getOpenPrice() - cPoint.getOpenPrice();
    } else {
      return cPoint.getOpenPrice() - dPoint.getOpenPrice();
    }
  }

  public double getCDPriceDifferenceRatio() {
    return Math.round(getCDPriceDifference() * 10000 / bPoint.getAdjustedClosePrice()) / 100.;
  }

  public int getYear() {
    return new Date(aPoint.getStartTimestamp()).getYear() + 1900;
  }

  public boolean isLong() {
    return isLong;
  }

  public double getFirstTradeDayRatio() {
    return firstTradeDayRatio;
  }

  public void setFirstTradeDayRatio(double firstTradeDayRatio) {
    this.firstTradeDayRatio = firstTradeDayRatio;
  }

  public boolean isFinalizing() {
    return isFinalizing;
  }

  public void setFinalizing(boolean isFinalizing) {
    this.isFinalizing = isFinalizing;
  }

  public double getLongShortRatio() {
    return longShortRatio;
  }

  public void setLongShortRatio(double longShortRatio) {
    this.longShortRatio = longShortRatio;
  }

}
