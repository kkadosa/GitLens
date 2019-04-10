package hu.bme.mit.gitlens;

import java.io.File;

public interface Lens {
	
	public void get(File f);

	public void put(File gold, File front);
	
}
