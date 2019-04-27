package hu.bme.mit.gitlens.auth;

import hu.bme.mit.gitlens.ColossalLens;
import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.LargeLens;
import hu.bme.mit.gitlens.Repo;
import hu.bme.mit.gitlens.ServerResponse;

public class ColossalLensImpl implements ColossalLens {
	private LargeLens largeLens = new LargeLensImpl();

	@Override
	public void get(Repo gold, Repo front) {
		for (Commit goldBranchHead : gold.getBranchHeads()) {
			String name = goldBranchHead.getName();
			if (!front.hasBranch(name)) {
				Commit root = gold.getLastMatchingAncestorTo(goldBranchHead);
				front.createBranch(root, name);
			}
			Commit frontBranchHead = front.getBranchHead(name);
			if (!frontBranchHead.matches(goldBranchHead)) {
				// TODO: full tree walk
				Commit newHead = largeLens.get(gold, goldBranchHead, front, frontBranchHead);
				front.pushBranch(name, newHead);
			}
		}
	}

	@Override
	public void put(Repo gold, Repo front) {
		for (Commit frontBranchHead : front.getBranchHeads()) {
			String name = frontBranchHead.getName();
			if (gold.hasBranch(name)) {
				// TODO branch making rights?
				Commit goldBranchHead = gold.getBranchHead(name);
				if (!frontBranchHead.matches(goldBranchHead)) {
					// TODO: full tree walk
					Commit newHead = largeLens.put(gold, goldBranchHead, front, frontBranchHead);
					gold.pushBranch(name, newHead);
				}
			}
		}
	}

	@Override
	public ServerResponse checkAuthorization(Repo repo, String branch, String oldSHA, String newSHA) {
		ServerResponse sp = new ServerResponse();
		sp.Response = largeLens.checkAuthorization(repo, repo.getCommit(oldSHA), repo.getCommit(newSHA));
		if(sp.Response != null) {
			sp.ReturnValue = 1;
		} else {
			repo.processAddedData(branch, newSHA);
			sp.ReturnValue = 0;
		}
		return sp;
	}
}
