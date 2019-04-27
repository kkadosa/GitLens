package hu.bme.mit.gitlens.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import hu.bme.mit.gitlens.Lens;

public class HiddenLens implements Lens{

	@Override
	public void get(Path gold, Path front) {
		try {
			Files.deleteIfExists(front);
		} catch (IOException e) {
			// ?s
		}
	}

	@Override
	public void put(Path gold, Path front) {
		;
	}

	@Override
	public String checkAuthorization(Path older, Path newer) {
		if (Files.exists(newer)) {
			String a = older.toString();
			String b = newer.toString();
			while(!a.endsWith(b)) {
				b = b.substring(1, b.length());
			}
			return "The file " + b + ", clashes with a file hidden from you. Either change its name, or give up.";
		} else {
			return null;
		}
	}
}
