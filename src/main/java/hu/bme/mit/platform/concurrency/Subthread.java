package hu.bme.mit.platform.concurrency;

import java.util.concurrent.CompletableFuture;

public abstract class Subthread<T>{

    BlockableThreadPool pool;

    protected void block(){
        ;
    }

    public abstract void run(CompletableFuture<T> future);
}
