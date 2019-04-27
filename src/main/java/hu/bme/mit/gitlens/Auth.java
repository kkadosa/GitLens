package hu.bme.mit.gitlens;

import java.nio.file.Path;


public interface Auth {

	Lens getLens(Repo repo, Path path);
	void addLens(Repo repo, Path path, Lens lens);
}
