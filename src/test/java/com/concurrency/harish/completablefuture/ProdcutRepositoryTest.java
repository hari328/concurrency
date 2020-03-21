package com.concurrency.harish.completablefuture;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ProdcutRepositoryTest {

  @Test
  public void getProductBlocking() {
    final ProdcutRepository prodcutRepository = new ProdcutRepository();
    final CompletableFuture<Product> productBlocking = prodcutRepository.getProductBlocking(22);
    productBlocking.thenAccept(result -> Assert.assertNotNull(result.getName()));
  }

  @Test
  public void getProductBlockingException() {
    final ProdcutRepository prodcutRepository = new ProdcutRepository();
    final CompletableFuture<Product> productBlocking = prodcutRepository.getProductBlocking(666);
    try {
      productBlocking.get();
    }
    catch (Exception e) {
      System.out.println("here");
      Assert.assertNotEquals(ExecutionException.class, e.getClass());
      Assert.assertEquals(RuntimeException.class, e.getCause().getClass());
    }
  }

  @Test
  public void getProductAsync() {
    final ProdcutRepository prodcutRepository = new ProdcutRepository();
    final CompletableFuture<Product> productBlocking = prodcutRepository.getProductAsync(22);

    //this is non blocking thenAccept which is only present in Completable future
    productBlocking.thenAccept(result -> Assert.assertNotNull(result.getName()));
  }

  @Test
  public void getProductAsyncEx() {
    final ProdcutRepository prodcutRepository = new ProdcutRepository();
    final CompletableFuture<Product> productBlocking = prodcutRepository.getProductAsync(666);

    //this is non blocking thenAccept which is only present in Completable future
      productBlocking
        .whenComplete((prodcut, ex)-> {
          if(ex != null) {
            System.out.println("got int ex");
            Assert.assertEquals(ClassCastException.class, ex.getClass());
            Assert.assertEquals(RuntimeException.class, ex.getCause().getClass());
          }else {
            return;
          }
        });
  }

  //highlight todo: this is how you check exceptions in completable future.
  @Test
  public void getProductAsyncTest() {
    final ProdcutRepository prodcutRepository = new ProdcutRepository();
    prodcutRepository.getProductAsync(666)
      .whenComplete((product, ex)-> {
        if(ex != null) {
          Assert.assertEquals(ExecutionException.class, ex.getClass());
          Assert.assertEquals(RuntimeException.class, ex.getCause().getClass());
        }
      });
  }
}