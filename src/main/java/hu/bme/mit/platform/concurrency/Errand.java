package hu.bme.mit.platform.concurrency;

import java.util.concurrent.CompletableFuture;

public abstract class Errand<T> implements Runnable{

    CompletableFuture<T> future = new CompletableFuture<>();

    @Override
    public abstract void run();
}