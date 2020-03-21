package com.concurrency.harish.completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class CompletableFutureUtil {
  public static  <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
    CompletableFuture<Void> allFuturesDone =
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
    return allFuturesDone.thenApply(v ->
      futures.stream()
        .map(CompletableFuture::join)
        .collect(toList()));
  }
}
