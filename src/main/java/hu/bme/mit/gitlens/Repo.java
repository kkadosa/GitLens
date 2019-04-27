package hu.bme.mit.gitlens;

import java.nio.file.Path;

public interface Repo {
	
	boolean isUpToDate();
	void refresh();
	void createBranch(Commit root, String name);
	boolean hasBranch(String branchName);
	void pushBranch(String name, Commit newHead);
	void checkOut(Commit commit);
	Lens getLens(Path p);
	Commit commit();
	void processAddedData(String branch, String newSHA);
	Iterable<Path> getDifferentPaths(Commit older, Commit newer);
	Iterable<Path> getAllPaths();
	Iterable<Commit> getBranchHeads();
	Commit getCommit(String SHA);
	Commit getMatchingCommit(Commit commit);
	Commit getBranchHead(String name);
	void readLock();
	void writeLock();
	void unlock();
	Path getRootPath();
	Path getTemporaryPath();
	String getName();
}
