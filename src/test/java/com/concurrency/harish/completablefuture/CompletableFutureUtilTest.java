package com.concurrency.harish.completablefuture;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompletableFutureUtilTest {

  @Test
  public void testSequence() {
    final ProdcutRepository prodcutRepository = new ProdcutRepository();
    final List<CompletableFuture<Product>> listOfFutures =
      IntStream.rangeClosed(1, 10).mapToObj(id -> prodcutRepository.getProductAsync(id)).collect(Collectors.toList());

    final CompletableFuture<List<Product>> results = CompletableFutureUtil.sequence(listOfFutures);

    results.thenAccept(products -> products.forEach(System.out::println));

    results.join();

  }

}