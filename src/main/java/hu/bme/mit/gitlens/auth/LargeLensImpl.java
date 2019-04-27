package hu.bme.mit.gitlens.auth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hu.bme.mit.gitlens.Commit;
import hu.bme.mit.gitlens.LargeLens;
import hu.bme.mit.gitlens.Lens;
import hu.bme.mit.gitlens.Repo;

public class LargeLensImpl implements LargeLens {

	@Override
	public Commit get(Repo gold, Commit infoSource, Repo front, Commit parentOfNew) {
		gold.checkOut(infoSource);
		front.checkOut(parentOfNew);
		Path goldRoot = gold.getRootPath();
		Path frontRoot = front.getRootPath();
		for (Path p : gold.getDifferentPaths(gold.getMatchingCommit(parentOfNew), infoSource)) {
			Path goldPath = goldRoot.resolve(p);
			Path frontPath = frontRoot.resolve(p);
			Lens lens = front.getLens(p);
			lens.get(goldPath, frontPath);
		}
		return front.commit();
	}

	@Override
	public Commit put(Repo gold, Commit parentOfNew, Repo front, Commit infoSource) {

		gold.checkOut(infoSource);
		front.checkOut(parentOfNew);
		Path goldRoot = gold.getRootPath();
		Path frontRoot = front.getRootPath();
		for (Path p : front.getDifferentPaths(front.getMatchingCommit(parentOfNew), infoSource)) {
			Path goldPath = goldRoot.resolve(p);
			Path frontPath = frontRoot.resolve(p);
			Lens lens = front.getLens(p);
			lens.get(goldPath, frontPath);
		}
		return gold.commit();
	}

	@Override
	public String checkAuthorization(Repo repo, Commit parent, Commit unvalidated) {
		try {
			String out = null;
			repo.checkOut(parent);
			Path rootPath = repo.getRootPath();
			Path tempRootPath = repo.getTemporaryPath();
			Files.createDirectory(tempRootPath);
			Iterable<Path> paths = repo.getDifferentPaths(parent, unvalidated);
			
			for (Path p : paths) {
				Path path = rootPath.resolve(p);
				Path tempPath = tempRootPath.resolve(p);
				if (path.toFile().exists()) {
					Files.copy(path, tempPath);
				}
			}

			repo.checkOut(unvalidated);
			List<String> responses = new ArrayList<String>();
			for (Path p : paths) {
				Path path = rootPath.resolve(p);
				Path tempPath = tempRootPath.resolve(p);
				Lens lens = repo.getLens(p);
				String temp = lens.checkAuthorization(tempPath, path);
				if (temp != null) {
					responses.add(temp);
				}
			}

			if (!responses.isEmpty()) {
				out = new String();
				for (String s : responses) {
					out = out + s + System.lineSeparator();
				}
			}

			Files.walk(tempRootPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);

			return out;
			
		} catch (IOException e) {
			// ?
			return "Sorry, Server Error";
		}
	}
}
