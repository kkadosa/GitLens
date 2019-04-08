package hu.bme.mit.gitlens.impl;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.Placeholder;
import hu.bme.mit.gitlens.Repo;

public class RepoImpl implements Repo{
	
	Placeholder graph;
	Map<String, Commit> branchHeads;

	@Override
	public boolean isUpToDate() {
		boolean out = false;
		ReadWriteLock lock = GitLensServiceImpl.LOCKS.get("");
		try {
			lock.readLock().lock();
			Repo gold = GitLensServiceImpl.REPOS.get("");
			out = gold.compareGraphs(graph);
		} finally {
			lock.readLock().unlock();
		}
		return out;
	}
	
	@Override
	public void refresh() {
		ReadWriteLock lock = GitLensServiceImpl.LOCKS.get("");
		try {
			lock.readLock().lock();
			Repo gold = GitLensServiceImpl.REPOS.get("");
			for(Commit branchHead : gold.getBranchHeads()) {
				Commit frontBranchHead = branchHeads.get(branchHead.getName());
				if(frontBranchHead != null) {
					//TODO make branch
					branchHeads.put(frontBranchHead.getName(), frontBranchHead);
				}
				if(!frontBranchHead.matches(frontBranchHead)) {
					Commit newHead = GitLensServiceImpl.COLOSSAL_LENS.get(gold, branchHead, this, frontBranchHead);
					branchHeads.replace(frontBranchHead.getName(), newHead);
				}
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean compareGraphs(Placeholder graph) {
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
}
