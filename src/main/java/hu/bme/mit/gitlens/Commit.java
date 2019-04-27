package hu.bme.mit.gitlens;

public interface Commit {
	
	boolean matches(Commit head);
	String getName();
	String getSHA();
	boolean isMergeCommit();
	Commit getAncestor();
	Commit getAncestor(Integer i);
	Repo getRepo();
}
