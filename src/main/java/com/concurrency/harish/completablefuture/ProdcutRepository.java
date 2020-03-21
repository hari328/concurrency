package com.concurrency.harish.completablefuture;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ProdcutRepository {
  private Map<Integer, Product> cache = new HashMap<>();

  public ProdcutRepository() {
  }

  private Product getLocal(int id) {
    return cache.get(id);
  }

  private Product getRemote(int id) {
    try {
      Thread.sleep(100);
      if (id == 666) {
        System.out.println("wrong id");
        throw new RuntimeException("Evil request");
      }
    } catch (InterruptedException ignored) {
    }
    final int val = new Random().nextInt();
    return new Product(id, "name"+val);
  }

  public CompletableFuture<Product> getRemotcAsync(int id) {
    final CompletableFuture<Product> productCompletableFuture = new CompletableFuture<>();
    try {
      Thread.sleep(100);
      if (id == 666) {
        System.out.println("wrong id");
        productCompletableFuture.completeExceptionally(new RuntimeException("Evil request"));
      }
    } catch (InterruptedException ignored) {
      productCompletableFuture.completeExceptionally(ignored);
    }
    final int val = new Random().nextInt();
    productCompletableFuture.complete(new Product(id, "name"+val));
    return productCompletableFuture;
  }

  //sync return
  public CompletableFuture<Product> getProductBlocking(int id) {
    try {

      final Product product = getLocal(id);
      if (product != null) {
        return CompletableFuture.completedFuture(product);
      }

      final Product retrievedProduct = getRemote(id);
      cache.put(retrievedProduct.getId(), retrievedProduct);
      return CompletableFuture.completedFuture(retrievedProduct);
    }
    catch (Exception ex) {
      final CompletableFuture<Product> productCompletableFuture = new CompletableFuture<>();
      productCompletableFuture.completeExceptionally(ex);
      return productCompletableFuture;
    }
  }


  //sync return
  public CompletableFuture<Product> getProductAsync(int id) {
    try {
      Product product = getLocal(id);
      if (product != null) {
        return CompletableFuture.completedFuture(product);
      } else {
//        System.out.println("test call"+Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
//          System.out.println("remote call"+Thread.currentThread().getName());
          Product p = getRemote(id);
          cache.put(id, p);
          return p;
        });

      }
    } catch (Exception e) {
      CompletableFuture<Product> future = new CompletableFuture<>();
      future.completeExceptionally(e);
      return future;
    }
  }

}
