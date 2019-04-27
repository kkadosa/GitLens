package hu.bme.mit.gitlens.auth;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.bme.mit.gitlens.Auth;
import hu.bme.mit.gitlens.Lens;
import hu.bme.mit.gitlens.Repo;

public class AuthImpl implements Auth {
	Map<Pair, Lens> lenses = new HashMap<Pair, Lens>();
	List<Repo> repos = new ArrayList<Repo>();
	
	@Override
	public Lens getLens(Repo repo, Path path) {
		Pair ho = new Pair(repo, path);
		Lens out = lenses.get(ho);
		if(out == null) {
			out = new AllActionsAllowedLens();
			lenses.put(ho, out);
			for(Repo r : repos) {
				if(!r.equals(repo)) {
					lenses.put(new Pair(r, path), new HiddenLens());
				}
			}
		}
		return out;
	}

	@Override
	public void addLens(Repo repo, Path path, Lens lens) {
		lenses.put(new Pair(repo, path), lens);
	}
	
	private class Pair{
		Repo repo;
		Path path;
		Pair(Repo repo, Path path) {
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
