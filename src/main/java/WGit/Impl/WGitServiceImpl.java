package WGit.Impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.thrift.TException;

import WGit.ServerResponse;
import WGit.Modes;
import WGit.Repo;
import WGit.Auth.GreatLens;

public class WGitServiceImpl implements WGit.WGitService.Iface{
	
	public static final Map<String, ReadWriteLock> Locks = Collections.synchronizedMap(new HashMap<String, ReadWriteLock>());
	public static final Map<String, Repo> Repos = Collections.synchronizedMap(new HashMap<String, Repo>());
	public static final GreatLens GreatLens = new GreatLens();
	
	@Override
	public ServerResponse AnswerAccess1(String repoName, String userName, Modes mode, String result) throws TException {
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
	public ServerResponse AnswerPreGit(String repoName, String userName, Modes mode, Modes command) throws TException {
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}

	@Override
	public ServerResponse AnswerAccess2(String repoName, String userName, Modes mode, String ref, String result,
			String oldCommit, String newCommit) throws TException {
		ServerResponse response = WriteResponse();	
		return response;
	}

	@Override
	public ServerResponse AnswerPostGit(String repoName, String userName, Modes mode, Modes command) throws TException {
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}

	private ServerResponse WriteResponse() {
		ServerResponse response = new ServerResponse();
		/*if(result != 0) {
			//check only correct files written
			//if ok, propagate to gold
			
			
			
			
		} else {
			response.Response = "You don't have any write access to this repo!";
			response.ReturnValue = 1;
		}*/
		return response;
	}

	private ServerResponse ReadResponse(String user, String repoName, String result) {
		if(!result.equals("DENIED")) {
			ReadWriteLock lock = Locks.computeIfAbsent(repoName, (name) -> new ReentrantReadWriteLock());
			Repo repo = Repos.computeIfAbsent(repoName, (name) -> new RepoImpl());
			boolean fresh = false;
			try {
				lock.readLock().lock();
				fresh = repo.IsUpToDate();
			} finally {
				lock.readLock().unlock();
			}
			try {
				if(!fresh) {
					lock.writeLock().lock();
					repo.Refresh();
				}
			} finally {
				lock.writeLock().unlock();
			}			
		}
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}	
}