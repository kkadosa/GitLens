package hu.bme.mit.platform.concurrency;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class BlockableThreadPool {

    ForkJoinPool pool = ForkJoinPool.commonPool();

    public <T> Future<T> submit(Errand<T> st){
        pool.submit(st);
        return st.future;
    }
}