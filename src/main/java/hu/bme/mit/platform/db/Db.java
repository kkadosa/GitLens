package hu.bme.mit.platform.db;

public class Db {
    //TODO database management

    public Db() {

    }

    public Repository getRepository(String uri) {
        Repository t = new Repository();
        t.isValid = true;
        return t;
    }
}