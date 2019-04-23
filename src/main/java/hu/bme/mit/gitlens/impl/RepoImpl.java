package hu.bme.mit.gitlens.impl;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.Placeholder;
import hu.bme.mit.gitlens.Repo;

public class RepoImpl implements Repo{
	
	Placeholder graph;
	Map<String, Commit> branchHeads;
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public boolean isUpToDate() {
		boolean out = false;
		Repo gold = GitLensServiceImpl.repos.get("");
		try {
			gold.readLock();
			out = compareGraphs(gold);
		} finally {
			gold.unlock();
		}
		return out;
	}
	
	@Override
	public void refresh() {
		Repo gold = GitLensServiceImpl.repos.get("");
		GitLensServiceImpl.COLOSSAL_LENS.get(gold, this);
	}


	private boolean compareGraphs(Repo otherRepo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Commit> getBranchHeads() {
		return branchHeads.values();
	}

	@Override
	public boolean isAuthorized(String oldSHA, String newSHA) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasBranch(String branchName) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public Commit getCommit(String SHA) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Commit getMatchingCommit(String SHA) {
		// TODO Auto-generated method stub
		return null;
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
			// TODO: specify exception 
		}
		try {
			lock.writeLock().unlock();
		} catch (Exception e) {
			//maybe expected?
			// TODO: specify exception
		}		
	}

	@Override
	public Commit getLastMatchingAncestor(Repo from, Commit local) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createBranch(Commit root, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Commit getBranchHead(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushBranch(String name, Commit newHead) {
		// TODO Auto-generated method stub
		
	}	
}
