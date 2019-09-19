package hu.bme.mit.platform.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class BlockableThreadPool {

    public <T> Future<T> submit(Subthread<T> st){
        CompletableFuture<T> ct = new CompletableFuture<>();
        return ct;
    }
}
