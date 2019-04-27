package hu.bme.mit.gitlens;

import java.nio.file.Path;

public interface Lens {
	
	public void get(Path gold, Path front);
	public void put(Path gold, Path front);
	public String checkAuthorization(Path older, Path newer);
}
