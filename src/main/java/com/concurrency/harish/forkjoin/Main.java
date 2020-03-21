package com.concurrency.harish.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    setPoolSizeProgramtically();
    settingPropertyForPoolSize();
  }

  private static void setPoolSizeProgramtically() throws InterruptedException, ExecutionException {
    //how to create fork join pool of arbitrary size?
    final ForkJoinPool forkJoinPool = new ForkJoinPool(20);

    final ForkJoinTask<Integer> sum = forkJoinPool.submit(() -> IntStream.rangeClosed(1, 10000).parallel().sum());

    //if we comment follow method the pool size is 1 that is main thread size.
    sum.get();
    System.out.println("program pool size is "+ forkJoinPool.getPoolSize());
  }


  public static void settingPropertyForPoolSize() {
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
    long total = LongStream.rangeClosed(1, 3_000_000)
      .parallel()
      .sum();

    int poolSize = ForkJoinPool.commonPool().getPoolSize();
    System.out.println("property Pool size: " + poolSize);
  }
}
