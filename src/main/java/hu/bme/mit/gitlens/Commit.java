package hu.bme.mit.gitlens;

public interface Commit {

	boolean matches(Commit head);

	String getName();

}
