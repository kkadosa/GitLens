package hu.bme.mit.platform.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

//syntax test class, to be deleted
public class Test {

    Errand<String> g = new Errand<String>() {
        @Override
        public void run() {
            future.exceptionally(Throwable::getMessage);
        }
    };

    public void Taw(){
        BlockableThreadPool btp = new BlockableThreadPool();
        Trt t = new Trt();
        Future<String> f = btp.submit(t);
    }

    public static class Trt extends Errand<String> {

        @Override
        public void run() {
            future.complete("terra");
        }
    }
}

