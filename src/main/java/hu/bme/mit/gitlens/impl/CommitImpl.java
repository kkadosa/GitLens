package hu.bme.mit.gitlens.impl;

import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.Repo;

public class CommitImpl implements Commit{
	String SHA;
	String name;
	Commit ancestor;
	Commit secondaryAncestor;
	Repo repo;
	
	@Override
	public boolean matches(Commit commit) {
		return SHA.equals(commit.getSHA());
	}
	
	@Override
	public boolean isMergeCommit() {
		return secondaryAncestor != null;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSHA() {
		return SHA;
	}

	@Override
	public Commit getAncestor() {
		return ancestor;
	}

	@Override
	public Repo getRepo() {
		return repo;
	}

	@Override
	public Commit getSecondaryAncestor() {
		return secondaryAncestor;
	}
}
