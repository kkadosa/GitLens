package hu.bme.mit.gitlens;

public interface Repo {
	public boolean isUpToDate();
	public void refresh();
	public boolean compareGraphs(Placeholder graph);
	public Iterable<Commit> getHeads();
	//get ordered list?
}
