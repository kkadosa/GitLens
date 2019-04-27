package hu.bme.mit.gitlens;

public interface LargeLens {
	
	public Commit get(Repo gold, Commit parentOfNew, Repo front, Commit infoSource);

	public Commit put(Repo gold, Commit infoSource, Repo front, Commit parentOfNew);

	public String checkAuthorization(Repo repo, Commit parent, Commit unvalidated);
}
