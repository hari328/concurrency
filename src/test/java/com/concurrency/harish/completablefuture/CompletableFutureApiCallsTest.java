package com.concurrency.harish.completablefuture;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.asynchttpclient.Dsl.*;

public class CompletableFutureApiCallsTest {
  @Test
  public void simpleGet() {
    AsyncHttpClient c = asyncHttpClient();

    final CompletableFuture<Response> future = c
      .prepareGet("http://www.google.com").execute()
      .toCompletableFuture()
      .thenApply(response -> {
        System.out.println(response.getResponseBody());
        return response;
      });

    future.join();
  }
}
