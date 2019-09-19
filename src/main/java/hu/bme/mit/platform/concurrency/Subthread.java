package hu.bme.mit.platform.concurrency;

public abstract class Subthread implements Runnable{

    BlockableThreadPool pool;

    protected void block(){
        ;
    }
}
