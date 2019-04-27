package hu.bme.mit.gitlens;

import java.nio.file.Path;

public interface Repo {
	
	boolean isUpToDate();
	void refresh();
	Iterable<Commit> getBranchHeads();
	boolean hasBranch(String branchName);
	Commit getCommit(String SHA);
	Commit getMatchingCommit(String SHA);
	void readLock();
	void writeLock();
	void unlock();
	Commit getLastMatchingAncestorTo(Commit local);
	void createBranch(Commit root, String name);
	Commit getBranchHead(String name);
	void pushBranch(String name, Commit newHead);
	Iterable<Path> getAllPaths();
	Path getRootPath();
	void checkOut(Commit commit);
	Lens getLens(Path p);
	Commit commit();
	Iterable<Path> getDifferentPaths(Commit older, Commit newer);
	String getName();
	void processAddedData(String branch, String newSHA);
	Path getTemporaryPath();
}
