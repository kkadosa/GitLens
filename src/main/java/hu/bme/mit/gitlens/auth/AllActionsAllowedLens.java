package hu.bme.mit.gitlens.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import hu.bme.mit.gitlens.Lens;

public class AllActionsAllowedLens implements Lens{

	@Override
	public void get(Path gold, Path front) {
		try {
			if (Files.exists(gold)) {
				Files.copy(gold, front, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.deleteIfExists(front);
			}
		} catch (IOException e) {
			// ?s
		}
	}

	@Override
	public void put(Path gold, Path front) {
		try {
			if (Files.exists(front)) {
				Files.copy(front, gold, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.deleteIfExists(gold);
			}
		} catch (IOException e) {
			// ?s
		}
	}

	@Override
	public String checkAuthorization(Path older, Path newer) {
		return null;
	}
}
