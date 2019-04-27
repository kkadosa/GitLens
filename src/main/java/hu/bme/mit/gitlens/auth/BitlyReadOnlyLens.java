package hu.bme.mit.gitlens.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import hu.bme.mit.gitlens.Lens;

public class BitlyReadOnlyLens implements Lens{

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
		;
	}

	@Override
	public String checkAuthorization(Path older, Path newer) {
		try {
			if (Files.exists(newer)) {
				byte[] of = Files.readAllBytes(older);
				byte[] nf = Files.readAllBytes(newer);
				if(Arrays.equals(of, nf)) {
					return null;
				} else {
					String a = older.toString();
					String b = newer.toString();
					while(!a.endsWith(b)) {
						b = b.substring(1, b.length());
					}
					return "You changed " + b + ", which you have no authrity for";
				}				
			} else {
				String a = older.toString();
				String b = newer.toString();
				while(!a.endsWith(b)) {
					b = b.substring(1, b.length());
				}
				return "You deleted " + b + ", which you have no authrity for";
			}
			
		} catch (IOException e) {
			// ?s
			return "Sorry, Server Error!";
		}
	}
}
