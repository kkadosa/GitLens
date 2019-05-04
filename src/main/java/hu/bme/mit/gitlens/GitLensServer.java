package hu.bme.mit.gitlens;


public interface GitLensServer {
	String getUser();
	Iterable<Repo> getRepos();
	Iterable<Repo> getRepos(String project);
	ColossalLens getColossalLens();
	Auth getAuth();
	Repo getRepo(String repoName);
}
