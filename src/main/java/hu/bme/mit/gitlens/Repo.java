package hu.bme.mit.gitlens;

public interface Repo {
	public boolean isUpToDate();
	public void refresh();
	public Iterable<Commit> getBranchHeads();
	//get ordered list?
	public boolean hasBranch(String branchName);
	public Commit getCommit(String SHA);
	public Commit getMatchingCommit(String SHA);
	public void readLock();
	public void unlock();
	public void writeLock();
	public Commit getLastMatchingAncestor(Repo from, Commit local);
	public void createBranch(Commit root, String name);
	public Commit getBranchHead(String name);
	public void pushBranch(String name, Commit newHead);
}
