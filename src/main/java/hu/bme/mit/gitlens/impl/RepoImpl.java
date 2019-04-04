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

	@Override
	public boolean isUpToDate() {
		boolean out = false;
		ReadWriteLock lock = GitLensServiceImpl.LOCKS.computeIfAbsent("", (name) -> new ReentrantReadWriteLock());
		try {
			lock.readLock().lock();
			Repo gold = GitLensServiceImpl.REPOS.computeIfAbsent("", (name) -> new RepoImpl());
			out = gold.compareGraphs(graph);
		} finally {
			lock.readLock().unlock();
		}
		return out;
	}
	
	@Override
	public void refresh() {
		ReadWriteLock lock = GitLensServiceImpl.LOCKS.computeIfAbsent("", (name) -> new ReentrantReadWriteLock());
		try {
			lock.readLock().lock();
			Repo gold = GitLensServiceImpl.REPOS.computeIfAbsent("", (name) -> new RepoImpl());
			for(Commit head : gold.getHeads()) {
				Commit here = branchHeads.get(head.getName());
				if(here != null) {
					//TODO make branch
					branchHeads.put(head.getName(), here);
				}
				if(!here.matches(head)) {
					Commit newHead = GitLensServiceImpl.COLOSSAL_LENSE.get(gold, head, this, here);
					branchHeads.replace(head.getName(), newHead);
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
	public Iterable<Commit> getHeads() {
		return branchHeads.values();
	}

}
