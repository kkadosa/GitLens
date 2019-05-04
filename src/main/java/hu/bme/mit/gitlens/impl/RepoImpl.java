package hu.bme.mit.gitlens.impl;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import hu.bme.mit.gitlens.Auth;
import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.GitLensServer;
import hu.bme.mit.gitlens.Lens;
import hu.bme.mit.gitlens.Repo;

public class RepoImpl implements Repo {

	private Map<String, Commit> branchHeads = new HashMap<String, Commit>();
	private Map<String, Commit> commits = new HashMap<String, Commit>();
	private Map<String, Commit> commitsByMatches = new HashMap<String, Commit>();
	private Map<Path, Lens> lenses = new HashMap<Path, Lens>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private GitLensServer server;
	private Path root;
	private Path tempPath;
	private String name;
	private String project;
	private Repository phys;
	private String user;
 
	public RepoImpl(String project, String user, Path parentOfRoot, GitLensServer server) {
		this.project = project;
		this.user = user;
		this.server = server;
		root = parentOfRoot.resolve(project + user);
		tempPath = parentOfRoot.resolve(project + user + "temp");
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			File rooter = root.toFile();
			phys = builder.setGitDir(rooter).readEnvironment().findGitDir().build();
			RevWalk rw = new RevWalk(phys);
			for(RevCommit c : rw) {
				//TODO !!!!!!!!!! 
			}
			
			
			
			//phys.getRefDatabase().getRefsByPrefix("refs/heads");
		} catch (Exception e) {
			// TODO z robbb
			e.printStackTrace();
		}
	}

	@Override
	public boolean isUpToDate() {
		boolean out = true;
		Repo gold = server.getRepo(project);
		try {
			gold.readLock();
			for (Commit banchHead : gold.getBranchHeads()) {
				if (out) {
					Commit localHead = branchHeads.get(banchHead.getName());
					if (localHead != null) {
						if (!localHead.matches(banchHead)) {
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
		Repo gold = server.getRepo(project);
		try {
			gold.readLock();
			server.getColossalLens().get(gold, this);
		} finally {
			gold.unlock();
		}
	}

	@Override
	public void createBranch(Commit root, String name) {
		root.setName(name);
		branchHeads.put(name, root);

		// TODO jgit make branch
	}

	@Override
	public boolean hasBranch(String name) {
		if (branchHeads.containsKey(name)) {
			return true;
		} else {
			// TODO refresh the map from file
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
	public Lens getLens(Path path) {
		Lens out = lenses.get(path);
		if (out == null) {
			Auth auth = server.getAuth();
			out = auth.getLens(this, path);
			lenses.put(path, out);
		}
		return out;
	}

	@Override
	public Commit commit(String lensedFrom) {
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
		return lenses.keySet();
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
			// maybe expected?
		}
		try {
			lock.writeLock().unlock();
		} catch (Exception e) {
			// maybe expected?
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

	@Override
	public String getProject() {
		return project;
	}
}
