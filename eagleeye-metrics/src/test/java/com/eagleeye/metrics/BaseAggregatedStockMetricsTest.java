package com.eagleeye.metrics;

import org.junit.Assert;
import org.junit.Test;

import com.eagleeye.metrics.BaseAggregatedStockMetrics;
import com.eagleeye.metrics.BaseStockMetrics;


public class BaseAggregatedStockMetricsTest {

  @Test
  public void testAggregate() {
    BaseAggregatedStockMetrics aggMetrics = new BaseAggregatedStockMetrics(100, 1000);
    BaseStockMetrics m = new BaseStockMetrics(1, 100);
    Assert.assertFalse(aggMetrics.canAggregate(m));
    m = new BaseStockMetrics(100, 200);
    Assert.assertTrue(aggMetrics.canAggregate(m));
    m.setClosePrice(1000.);
    m.setAdjustedClosePrice(2000.);
    m.setOpenPrice(3000.);
    m.setVolume(100L);
    m.setHighestPrice(4000.);
    m.setLowestPrice(100.);
    aggMetrics.aggregate(m);

    Assert.assertEquals(1000., aggMetrics.getClosePrice(), 0.0001);
    Assert.assertEquals(1000., aggMetrics.getHighestClosePrice(), 0.0001);
    Assert.assertEquals(1000., aggMetrics.getLowestClosePrice(), 0.0001);
    Assert.assertEquals(2000., aggMetrics.getAdjustedClosePrice(), 0.0001);
    Assert.assertEquals(2000., aggMetrics.getHighestAdjustedClosePrice(), 0.0001);
    Assert.assertEquals(2000., aggMetrics.getLowestAdjustedClosePrice(), 0.0001);
    Assert.assertEquals(3000., aggMetrics.getOpenPrice(), 0.0001);
    Assert.assertEquals(3000., aggMetrics.getHighestOpenPrice(), 0.0001);
    Assert.assertEquals(3000., aggMetrics.getLowestOpenPrice(), 0.0001);

    Assert.assertEquals(100L, (long) aggMetrics.getVolume());
    Assert.assertEquals(4000., aggMetrics.getHighestPrice(), 0.0001);
    Assert.assertEquals(100., aggMetrics.getLowestPrice(), 0.0001);

    m = new BaseStockMetrics(200, 300);
    Assert.assertTrue(aggMetrics.canAggregate(m));
    m.setClosePrice(1001.);
    m.setAdjustedClosePrice(2001.);
    m.setOpenPrice(3001.);
    m.setVolume(200L);
    m.setHighestPrice(4001.);
    m.setLowestPrice(101.);
    aggMetrics.aggregate(m);

    Assert.assertEquals(1001., aggMetrics.getClosePrice(), 0.0001);
    Assert.assertEquals(1001., aggMetrics.getHighestClosePrice(), 0.0001);
    Assert.assertEquals(1000., aggMetrics.getLowestClosePrice(), 0.0001);
    Assert.assertEquals(2001., aggMetrics.getAdjustedClosePrice(), 0.0001);
    Assert.assertEquals(2001., aggMetrics.getHighestAdjustedClosePrice(), 0.0001);
    Assert.assertEquals(2000., aggMetrics.getLowestAdjustedClosePrice(), 0.0001);
    Assert.assertEquals(3001., aggMetrics.getOpenPrice(), 0.0001);
    Assert.assertEquals(3001., aggMetrics.getHighestOpenPrice(), 0.0001);
    Assert.assertEquals(3000., aggMetrics.getLowestOpenPrice(), 0.0001);

    Assert.assertEquals(300L, (long) aggMetrics.getVolume());
    Assert.assertEquals(4001., aggMetrics.getHighestPrice(), 0.0001);
    Assert.assertEquals(100., aggMetrics.getLowestPrice(), 0.0001);


  }
}
