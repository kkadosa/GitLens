package WGit;

public interface Repo {
	public boolean IsUpToDate();
	public void Refresh();
	public boolean CompareGraphs(Placeholder graph);
	public Iterable<Commit> getHeads();
	//get ordered list?
}
