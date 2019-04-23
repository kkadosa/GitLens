package hu.bme.mit.gitlens;

public interface ColossalLens {
	
	public void get(Repo gold, Repo front);

	public void put(Repo gold, Repo front);

	public ServerResponse isAuthorized(Repo repo, String oldSHA, String newSHA);

}
