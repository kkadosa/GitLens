package hu.bme.mit.platform.concurrency;

import java.util.concurrent.CompletableFuture;

public abstract class Errand<T> {

    BlockableThreadPool pool = null;

    public abstract void run(CompletableFuture<T> future);
}
