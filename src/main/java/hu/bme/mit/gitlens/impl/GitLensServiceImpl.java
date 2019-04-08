package hu.bme.mit.gitlens.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.thrift.TException;

import hu.bme.mit.gitlens.ColossalLens;
import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.Modes;
import hu.bme.mit.gitlens.Repo;
import hu.bme.mit.gitlens.ServerResponse;
import hu.bme.mit.gitlens.auth.ColossalLensImpl;

public class GitLensServiceImpl implements hu.bme.mit.gitlens.GitLensService.Iface{
	
	public static final Map<String, ReadWriteLock> LOCKS = Collections.synchronizedMap(new HashMap<String, ReadWriteLock>());
	public static final Map<String, Repo> REPOS = Collections.synchronizedMap(new HashMap<String, Repo>());
	public static final ColossalLens COLOSSAL_LENS = new ColossalLensImpl();
	
	//TODO crete gold repo on startup
	
	@Override
	public ServerResponse answerAccess1(String repoName, String userName, Modes mode, String result) throws TException {
		ServerResponse response;
		if(mode == Modes.R) {
			response = ReadResponse(repoName, userName, result);
		} else {
			response = new ServerResponse();
			response.ReturnValue = 0;
		}
		return response;
	}

	@Override
	public ServerResponse answerAccess2(String repoName, String userName, Modes mode, String ref, String result,
			String oldCommit, String newCommit) throws TException {
		//TODO check mode
		String[] temp = ref.split("/");
		ServerResponse response = WriteResponse(repoName, userName, temp[temp.length-1], result, oldCommit, newCommit);
		return response;
	}

	private ServerResponse WriteResponse(String repoName, String userName, String branchName, String result,
			String oldSHA, String newSHA) {
		ServerResponse response = new ServerResponse();
		if (!result.equals("DENIED")) {
			ReadWriteLock lock = LOCKS.computeIfAbsent(repoName, (name) -> new ReentrantReadWriteLock());
			Repo repo = REPOS.computeIfAbsent(repoName, (name) -> new RepoImpl());
			boolean propagate = false;
			try {
				lock.readLock().lock();
				if (repo.isUpToDate()) {
					if (repo.isAuthorized(oldSHA, newSHA)) {
						response.ReturnValue = 0;
						//TODO update Repo object's tree and such
						ReadWriteLock goldLock = LOCKS.get("");
						try {
							goldLock.readLock().lock();
							Repo gold = REPOS.get("");
							propagate = gold.hasBranch(branchName);
						} finally {
							goldLock.readLock().unlock();
						}
						if (propagate) {
							try {
								goldLock.writeLock().lock();
								Repo gold = REPOS.get("");
								Commit newCommit = repo.getCommit(newSHA);
								Commit oldCommit = gold.getMatchingCommit(oldSHA);
								COLOSSAL_LENS.put(gold, oldCommit, repo, newCommit);
							} finally {
								goldLock.writeLock().unlock();
							}
						}
					}
				} else {
					response.Response = "Pull!";
					response.ReturnValue = 1;
				}
			} finally {
				lock.readLock().unlock();
			}
		} else {
			response.ReturnValue = 0;
		}
		return response;
	}

	private ServerResponse ReadResponse(String repoName, String user, String result) {
		if(!result.equals("DENIED")) {
			ReadWriteLock lock = LOCKS.computeIfAbsent(repoName, (name) -> new ReentrantReadWriteLock());
			Repo repo = REPOS.computeIfAbsent(repoName, (name) -> new RepoImpl());
			boolean fresh = false;
			try {
				lock.readLock().lock();
				fresh = repo.isUpToDate();
			} finally {
				lock.readLock().unlock();
			}
			if (!fresh) {
				try {
					lock.writeLock().lock();
					repo.refresh();
				} finally {
					lock.writeLock().unlock();
				}
			}
		}
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}

	@Override
	public ServerResponse answerPostGit(String repoName, String userName, Modes mode, Modes command) throws TException {
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}
	
	@Override
	public ServerResponse answerPreGit(String repoName, String userName, Modes mode, Modes command) throws TException {
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}
}