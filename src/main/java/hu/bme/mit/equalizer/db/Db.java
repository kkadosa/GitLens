package hu.bme.mit.equalizer.db;

public class Db {
    //TODO database management
    public Repository getRepository(String uri) {
        Repository t = new Repository();
        t.isValid = true;
        return t;
    }
}
