package hu.bme.mit.gitlens.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import hu.bme.mit.gitlens.Lens;

public class BitlyReadOnlyLens implements Lens {

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
				if(Files.isDirectory(newer)) {
					return null;
				} else {
					long size = Files.size(older);
					if (size == Files.size(newer)) {
						MessageDigest md = MessageDigest.getInstance("SHA-512");
						MessageDigest md2 = (MessageDigest) md.clone();
						DigestInputStream is = new DigestInputStream(Files.newInputStream(older), md);
						while(is.available() > 0) {
							is.read();
						}
						byte[] olderHash = md.digest();
						is.close();
						DigestInputStream is2 = new DigestInputStream(Files.newInputStream(newer), md2);
						while(is2.available() > 0) {
							is2.read();
						}
						is2.close();
						byte[] newerHash = md2.digest();						
						if(Arrays.equals(olderHash, newerHash)) {
							return null;
						}
					}
					String a = older.toString();
					String b = newer.toString();
					while (!a.endsWith(b)) {
						b = b.substring(1, b.length());
					}
					return "You changed " + b + ", which you have no authrity for";
				}
			} else {
				String a = older.toString();
				String b = newer.toString();
				while (!a.endsWith(b)) {
					b = b.substring(1, b.length());
				}
				return "You deleted " + b + ", which you have no authrity for";
			}

		} catch (Exception e) {
			// ?s
			return "Sorry, Server Error!";
		}
	}
}
