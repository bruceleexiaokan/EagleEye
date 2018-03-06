package com.eagleeye.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.eagleeye.event.BreakOutEvent;
import com.eagleeye.metrics.BaseStockMetrics;
import com.eagleeye.metrics.StockMetrics;


public class TestStreamDriver {

  public static void main(String[] args) throws IOException, ParseException {
    String stock = "BIIB";

    InputStream is = TestStreamDriver.class.getResourceAsStream(stock + ".csv");
    if (null == is) {
      is = TestStreamDriver.class.getResourceAsStream("/" + stock + ".csv");
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    boolean first = true;
    String line;
    StockMetricsHistory stream = new StockMetricsHistory(StreamType.DAILY, 1000000);
    LongDailyHistoryChecker longChecker = new LongDailyHistoryChecker(stream);
    ShortDailyHistoryChecker shortChecker = new ShortDailyHistoryChecker(stream);
    int longBreakOutCount = 0;
    int shortBreakOutCount = 0;
    List<BreakOutEvent> events = new ArrayList<>();;
    while ((line = reader.readLine()) != null) {
      if (first) {
        first = false;
        continue;
      }
      String[] fields = line.split(",");
      StockMetrics metrics = generateMetrics(fields);
      Assert.assertNotNull(metrics);
      stream.onMetrics(metrics);
      BreakOutEvent event = null;
      event = longChecker.breakOutCheck();
      if (event != null) {
        events.add(event);
        longBreakOutCount++;
      }
      event = null;
      event = shortChecker.breakOutCheck();
      if (event != null) {
        events.add(event);
        shortBreakOutCount++;
      }
    }

    int gainCount = 0;
    int lossCount = 0;
    double totalNetLoss = 0;
    double totalNetGain = 0;
    double totalGainRatio = 0;
    int minDays = -1;
    int maxDays = -1;
    int totalDays = 0;
    for (BreakOutEvent event : events) {
      double acVolumeRatio = Math.round((double) event.getCPoint().getVolume() / (double) event.getAPoint().getVolume() * 100.) / 100.;
      if (event.isLong()) {
        System.out.print("Long ");
      } else {
        System.out.print("Short ");
      }
      System.out
          .println("breakout date: " + dateFormat.format(new Date(event.getBPoint().getStartTimestamp())) + ", A-B ratio: " + event
              .getABPriceDifferenceRatio() + ", A-B days: " + event.getABDurationInDays() + ", C-D ratio: " + event
              .getCDPriceDifferenceRatio() + ", C-D days: " + event.getCDDurationInDays() + ", ratio of C/A volume: " + acVolumeRatio);
      System.out
          .println("A point date: " + dateFormat.format(new Date(event.getAPoint().getStartTimestamp())) + ", volume: " + event.getAPoint()
              .getVolume() + ", price: " + event.getAPoint().getAdjustedClosePrice() + ", B point date: " + dateFormat.format(new Date(
              event.getBPoint().getStartTimestamp())) + ", volume: " + event.getBPoint().getVolume() + ", price: " + event.getBPoint()
              .getAdjustedClosePrice() + ", C point date: " + dateFormat.format(new Date(event.getCPoint().getStartTimestamp())) + ", volume: " + event
              .getCPoint().getVolume() + ", price: " + event.getCPoint().getAdjustedClosePrice() + ", D point date: " + dateFormat
              .format(new Date(event.getDPoint().getStartTimestamp())) + ", volume: " + event.getDPoint().getVolume() + ", price: " + event
              .getDPoint().getAdjustedClosePrice());
      System.out.println("Long short ratio: " + event.getLongShortRatio());
      System.out.println();

      totalGainRatio += event.getCDPriceDifferenceRatio();
      if (event.getCDPriceDifferenceRatio() < 0) {
        lossCount++;
        totalNetLoss += event.getCDPriceDifferenceRatio();
      } else {
        gainCount++;
        totalNetGain += event.getCDPriceDifferenceRatio();
      }
      if (minDays == -1) {
        minDays = event.getCDDurationInDays();
        maxDays = event.getCDDurationInDays();;
      }
      minDays = Math.min(minDays, event.getCDDurationInDays());
      maxDays = Math.max(maxDays, event.getCDDurationInDays());
      totalDays += event.getCDDurationInDays();
    }
    totalGainRatio = Math.round(totalGainRatio * 100.) / 100.;
    totalNetLoss = Math.round(totalNetLoss * 100.) / 100.;
    totalNetGain = Math.round(totalNetGain * 100.) / 100.;
    System.out.println();
    System.out
        .println("Total breakout times: " + (longBreakOutCount + shortBreakOutCount) + ", number of gain: " + gainCount + ", number of loss: " + lossCount + ", total net loss: " + totalNetLoss + "%" + ", total net gain: " + totalNetGain + "%, total earning ratio: " + totalGainRatio + "%");
    if (!events.isEmpty()) {
      System.out
          .println("Average trade days: " + (totalDays / events.size()) + ", min trade days " + minDays + ", max trade days " + maxDays);
    }
    System.out.println("Total long breakout count: " + longBreakOutCount + ", total short breakout count: " + shortBreakOutCount);

  }

  private static StockMetrics generateMetrics(String[] fields) throws ParseException {
    String date = fields[0];
    String open = fields[1];
    String high = fields[2];
    String low = fields[3];
    String close = fields[4];
    String adjClose = fields[5];
    String volume = fields[6];

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // sdf.setTimeZone(MetricConstants.NEW_YORK_TIME_ZONE);
    Date d = sdf.parse(date);
    long startTime = d.getTime();
    long endTime = startTime + MetricConstants.MS_IN_ONE_DAY - 1;
    BaseStockMetrics metrics = new BaseStockMetrics(startTime, endTime);
    metrics.setOpenPrice(Double.valueOf(open));
    metrics.setHighestPrice(Double.valueOf(high));
    metrics.setLowestPrice(Double.valueOf(low));
    metrics.setClosePrice(Double.valueOf(close));
    metrics.setAdjustedClosePrice(Double.valueOf(adjClose));
    metrics.setVolume(Long.valueOf(volume));
    if (metrics.getAdjustedClosePrice() == null) {
      System.out.println("Error");
    }
    return metrics;
  }
}
