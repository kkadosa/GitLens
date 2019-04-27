package hu.bme.mit.gitlens.impl;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.Lens;
import hu.bme.mit.gitlens.Repo;

public class RepoImpl implements Repo{
	
	Map<String, Commit> branchHeads = new HashMap<String, Commit>();;
	Map<String, Commit> commits = new HashMap<String, Commit>();
	Map<String, Commit> commitsByMatches = new HashMap<String, Commit>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	Path root;
	Path tempPath;
	Map<Path, Lens> allPaths = new HashMap<Path, Lens>();
	String name;

	@Override
	public boolean isUpToDate() {
		boolean out = true;
		Repo gold = GitLensServiceImpl.repos.get("");
		try {
			gold.readLock();
			for(Commit banchHead : gold.getBranchHeads()) {
				if(out) {
					Commit localHead = branchHeads.get(banchHead.getName());
					if(localHead != null) {
						if(!localHead.matches(banchHead)) {
							out = false;
						}
					} else {
						out = false;
					}
				}
			}
		} finally {
			gold.unlock();
		}
		return out;
	}
	
	@Override
	public void refresh() {
		Repo gold = GitLensServiceImpl.repos.get("");
		try {
			gold.readLock();
			GitLensServiceImpl.COLOSSAL_LENS.get(gold, this);
		} finally {
			gold.unlock();
		}
	}

	@Override
	public void createBranch(Commit root, String name) {
		root.setName("name");
		//TODO jgit make branch
	}

	@Override
	public boolean hasBranch(String name) {
		if(branchHeads.containsKey(name)) {
			return true;
		} else {
			//TODO refresh the map from file
			return false;
		}
	}

	@Override
	public void pushBranch(String name, Commit newHead) {
		Commit old = branchHeads.get(name);
		old.setName(null);
		newHead.setName(name);
		branchHeads.put(name, newHead);
	}

	@Override
	public void checkOut(Commit commit) {
		// TODO jgit
	}

	@Override
	public Lens getLens(Path p) {
		// TODO auth class
		return null;
	}

	@Override
	public Commit commit() {
		// TODO jgit
		return null;
	}

	@Override
	public void processAddedData(String branch, String newSHA) {
		// TODO jgit refresh paths, add logical commits, push logical branch
	}

	@Override
	public Iterable<Path> getDifferentPaths(Commit older, Commit newer) {
		// TODO jgit diff
		return null;
	}

	@Override
	public Iterable<Path> getAllPaths() {
		return allPaths.keySet();
	}

	@Override
	public Iterable<Commit> getBranchHeads() {
		return branchHeads.values();
	}

	@Override
	public Commit getCommit(String SHA) {
		return commits.get(SHA);
	}

	@Override
	public Commit getMatchingCommit(Commit commit) {
		return commitsByMatches.get(commit.getSHA());
	}
	
	@Override
	public Commit getBranchHead(String name) {
		return branchHeads.get(name);
	}

	@Override
	public void readLock() {
		lock.readLock().lock();		
	}
	
	@Override
	public void writeLock() {
		lock.writeLock().lock();		
	}

	@Override
	public void unlock() {
		try {
			lock.readLock().unlock();
		} catch (Exception e) {
			//maybe expected?
		}
		try {
			lock.writeLock().unlock();
		} catch (Exception e) {
			//maybe expected?
		}		
	}

	@Override
	public Path getRootPath() {
		return root;
	}

	@Override
	public Path getTemporaryPath() {
		return tempPath;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
