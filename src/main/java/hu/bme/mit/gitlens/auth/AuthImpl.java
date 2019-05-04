package hu.bme.mit.gitlens.auth;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import hu.bme.mit.gitlens.Auth;
import hu.bme.mit.gitlens.GitLensServer;
import hu.bme.mit.gitlens.Lens;
import hu.bme.mit.gitlens.Repo;

public class AuthImpl implements Auth {
	Map<Pair, Lens> lenses = new HashMap<Pair, Lens>();
	GitLensServer server;
	
	//TODO z lens factory kind of thingie
	
	public AuthImpl(GitLensServer server) {
		this.server = server;
	}
	
	@Override
	public Lens getLens(Repo repo, Path path) {
		Pair ho = new Pair(repo.getName(), path);
		Lens out = lenses.get(ho);
		if(out == null) {
			out = new AllActionsAllowedLens();
			lenses.put(ho, out);
			for(Repo r : server.getRepos(repo.getProject())) {
				if(!r.equals(repo)) {
					lenses.put(new Pair(r.getName(), path), new HiddenLens());
				}
			}
		}
		return out;
	}

	@Override
	public void addLens(String repo, Path path, Lens lens) {
		lenses.put(new Pair(repo, path), lens);
	}
	
	private class Pair{
		String repo;
		Path path;
		Pair(String repo, Path path) {
			this.repo = repo;
			this.path = path;
		}
		@Override
		public boolean equals(Object obj) {
			Pair p = (Pair) obj;
			return p.repo.equals(this.repo) && p.path.equals(this.path);
			
		}
	}
}
