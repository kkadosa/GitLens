package hu.bme.mit.platform.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

//syntax test class, to be deleted
public class Test {

    public void Taw(){
        BlockableThreadPool btp = new BlockableThreadPool();
        Trt t = new Trt();
        Future<String> f = btp.submit(t);
    }

    public static class Trt extends Subthread<String> {

        @Override
        public void run(CompletableFuture<String> future) {
            future.complete("terra");
        }
    }
}

