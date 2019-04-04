package WGit;

public interface Commit {

	boolean Matches(Commit head);

	String getName();

}
