package hu.bme.mit.gitlens.impl;

import org.apache.thrift.TException;

import hu.bme.mit.gitlens.GitLensServer;
import hu.bme.mit.gitlens.Modes;
import hu.bme.mit.gitlens.Repo;
import hu.bme.mit.gitlens.ServerResponse;

public class GitLensServiceImpl implements hu.bme.mit.gitlens.GitLensService.Iface {
	GitLensServer server;
	
	public GitLensServiceImpl(GitLensServer server) {
		this.server = server;
	}

	@Override
	public ServerResponse answerAccess1(String repoName, String userName, Modes mode, String result) throws TException {
		if (mode == Modes.R && !result.equals("DENIED")) {
			Repo repo = server.getRepo(repoName);
			boolean fresh = false;
			try {
				repo.readLock();
				fresh = repo.isUpToDate();
			} finally {
				repo.unlock();
			}
			if (!fresh) {
				try {
					repo.writeLock();
					repo.refresh();
				} finally {
					repo.unlock();
				}
			}
		}
		ServerResponse response = new ServerResponse();
		response.ReturnValue = 0;
		return response;
	}

	@Override
	public ServerResponse answerAccess2(String repoName, String userName, Modes mode, String ref, String result,
			String oldCommit, String newCommit) throws TException {
		// TODO check mode
		ServerResponse response = new ServerResponse();
		if (!result.equals("DENIED")) {
			Repo repo = server.getRepo(repoName);
			try {
				repo.readLock();
				if (repo.isUpToDate()) {
					String[] temp = ref.split("/");
					response = server.getColossalLens().checkAuthorization(repo, temp[temp.length -1], oldCommit, newCommit);
					if (response.ReturnValue == 0) {
						Repo gold = server.getRepo(repo.getProject());
						try {
							gold.writeLock();
							server.getColossalLens().put(gold, repo);
						} finally {
							gold.unlock();
						}			
					}
				} else {
					response.Response = "Pull!";
					response.ReturnValue = 1;
				}
			} finally {
				repo.unlock();
			}
		} else {
			response.ReturnValue = 0;
		}
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