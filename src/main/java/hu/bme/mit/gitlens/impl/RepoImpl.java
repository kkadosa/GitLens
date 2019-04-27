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
	
	Map<String, Commit> branchHeads;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	Path root;
	Map<Path, Lens> allPaths = new HashMap<Path, Lens>();

	@Override
	public boolean isUpToDate() {
		boolean out = true;
		Repo gold = GitLensServiceImpl.repos.get("");
		try {
			gold.readLock();
			for(Commit banchHead : gold.getBranchHeads()) {
				if(out && !banchHead.matches(this.branchHeads.get(banchHead.getName()))) {
					//TODO branch existence checking
					out = false;
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
	public Iterable<Commit> getBranchHeads() {
		return branchHeads.values();
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
	public Commit getLastMatchingAncestorTo(Commit local) {
		Repo other = local.getRepo();
		Commit ancestor = other.getMatchingCommit(local.getSHA());
		while(ancestor == null) {
			if(!local.isMergeCommit()) {
				local = local.getAncestor();
			} else {
				local = getClosestCommonAncestor(local.getAncestor(1), local.getAncestor(2));
			}
			ancestor = other.getMatchingCommit(local.getSHA());
		}
		return ancestor;
	}

	private Commit getClosestCommonAncestor(Commit a, Commit b) {
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

	@Override
	public Iterable<Path> getAllPaths() {
		return allPaths.keySet();
	}

	@Override
	public Path getRootPath() {
		return root;
	}

	@Override
	public void checkOut(Commit commit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Lens getLens(Path p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Commit commit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Path> getDifferentPaths(Commit older, Commit newer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processAddedData(String branch, String newSHA) {
		// TODO refresh paths, add logical commits, push logical branch
	}

	@Override
	public Path getTemporaryPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
