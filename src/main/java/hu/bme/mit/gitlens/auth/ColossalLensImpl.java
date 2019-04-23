package hu.bme.mit.gitlens.auth;

import hu.bme.mit.gitlens.ColossalLens;
import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.LargeLens;
import hu.bme.mit.gitlens.Repo;

public class ColossalLensImpl implements ColossalLens{
	private LargeLens  ll = new LargeLensImpl();

	@Override
	public Commit get(Repo gold, Commit parentOfNew, Repo front, Commit infoSource) {
		return ll.get(gold, parentOfNew, front, infoSource);
	}

	@Override
	public void put(Repo gold, Commit infoSource, Repo front, Commit parentOfNew) {
		ll.put(gold, infoSource, front, parentOfNew);	
	}

}
