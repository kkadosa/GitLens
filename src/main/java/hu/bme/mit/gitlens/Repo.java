package hu.bme.mit.gitlens;

public interface Repo {
	public boolean isUpToDate();
	public void refresh();
	public boolean compareGraphs(Placeholder graph);
	public Iterable<Commit> getBranchHeads();
	//get ordered list?
	public boolean isAuthorized(String oldSHA, String newSHA);
	public boolean hasBranch(String branchName);
	public Commit getCommit(String SHA);
	public Commit getMatchingCommit(String SHA);
}
