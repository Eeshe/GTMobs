package me.eeshe.gtmobs.models.config;

import java.util.concurrent.ThreadLocalRandom;

public class IntRange {
  private final int min;
  private final int max;

  public IntRange(int a, int b) {
    this.min = Math.min(a, b);
    this.max = Math.max(a, b);
  }

  public int generateRandom() {
    if (min == max)
      return max;

    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }
}
