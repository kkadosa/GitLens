package hu.bme.mit.gitlens;

public interface Commit {
	
	boolean matches(Commit commit);
	boolean isMergeCommit();
	String getName();
	void setName(String name);
	String getSHA();
	Commit getAncestor();
	Commit getSecondaryAncestor();
	Repo getRepo();
}