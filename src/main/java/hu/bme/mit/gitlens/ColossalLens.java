package hu.bme.mit.gitlens;

public interface ColossalLens {
	
	public void get(Repo gold, Repo front);
	public void put(Repo gold, Repo front);
	ServerResponse checkAuthorization(Repo repo, String branch, String oldSHA, String newSHA);
}
