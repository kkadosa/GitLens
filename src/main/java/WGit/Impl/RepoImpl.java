package WGit.Impl;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import WGit.Commit;
import WGit.Placeholder;
import WGit.Repo;

public class RepoImpl implements Repo{
	
	Placeholder graph;
	Map<String, Commit> heads;
	
	public RepoImpl() {
		//get shit
	}
	
	
	
	@Override
	public boolean IsUpToDate() {
		boolean out = false;
		ReadWriteLock lock = WGitServiceImpl.Locks.computeIfAbsent("", (name) -> new ReentrantReadWriteLock());
		try {
			lock.readLock().lock();
			Repo gold = WGitServiceImpl.Repos.computeIfAbsent("", (name) -> new RepoImpl());
			out = gold.CompareGraphs(graph);
		} finally {
			lock.readLock().unlock();
		}
		return out;
	}

	@Override
	public void Refresh() {
		ReadWriteLock lock = WGitServiceImpl.Locks.computeIfAbsent("", (name) -> new ReentrantReadWriteLock());
		try {
			lock.readLock().lock();
			Repo gold = WGitServiceImpl.Repos.computeIfAbsent("", (name) -> new RepoImpl());
			for(Commit head : gold.getHeads()) {
				//? conc
				Commit here = heads.get(head.getName());
				if(here != null) {
					//make branch
					heads.put(head.getName(), here);
				}
				if(!here.Matches(head)) {
					Commit newHead = WGitServiceImpl.GreatLens.Get(gold, head, this, here);
					heads.replace(head.getName(), newHead);
				}
			}
		} finally {
			lock.readLock().unlock();
		}
	}



	@Override
	public boolean CompareGraphs(Placeholder graph) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public Iterable<Commit> getHeads() {
		return heads.values();
	}

}
