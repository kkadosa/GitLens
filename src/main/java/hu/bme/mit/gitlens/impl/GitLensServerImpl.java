package hu.bme.mit.gitlens.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import hu.bme.mit.gitlens.Auth;
import hu.bme.mit.gitlens.ColossalLens;
import hu.bme.mit.gitlens.GitLensServer;
import hu.bme.mit.gitlens.GitLensService;
import hu.bme.mit.gitlens.Lens;
import hu.bme.mit.gitlens.Repo;
import hu.bme.mit.gitlens.auth.AllActionsAllowedLens;
import hu.bme.mit.gitlens.auth.AuthImpl;
import hu.bme.mit.gitlens.auth.BitlyReadOnlyLens;
import hu.bme.mit.gitlens.auth.ColossalLensImpl;
import hu.bme.mit.gitlens.auth.HiddenLens;

public class GitLensServerImpl implements GitLensServer, Runnable {
	private static final int PORT = 12147;

	private Map<String, Repo> repos = new HashMap<String, Repo>();
	private ColossalLens colossalLens = new ColossalLensImpl();
	private Auth auth = new AuthImpl(this);
	private Path root = Paths.get("/home/koltai/Repositories/"); //TODO z

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		try {
			Init();
			GitLensServiceImpl service = new GitLensServiceImpl(this);
			TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);
			GitLensService.Processor processor = new GitLensService.Processor(service);
			THsHaServer.Args args = new THsHaServer.Args(socket);
			args.protocolFactory(new TBinaryProtocol.Factory());
			args.transportFactory(new TFramedTransport.Factory());
			args.processorFactory(new TProcessorFactory(processor));
			TServer server = new THsHaServer(args);
			System.out.println("Starting server on port " + PORT);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}

	private void Init() {
		// TODO z Ingest config, touch gitolite config etc
		Lens ro = new BitlyReadOnlyLens();
		Lens rw = new AllActionsAllowedLens();
		Lens n = new HiddenLens();
		
		Path pathA = Paths.get("a.txt");
		Path pathB = Paths.get("b.txt");
		Path pathC = Paths.get("c.txt");
		auth.addLens("TestProjA", pathA, rw);
		auth.addLens("TestProjA", pathB, rw);
		auth.addLens("TestProjA", pathC, rw);
		auth.addLens("TestProjB", pathA, rw);
		auth.addLens("TestProjB", pathB, rw);
		auth.addLens("TestProjB", pathC, rw);
		auth.addLens("TestProjC", pathA, rw);
		auth.addLens("TestProjC", pathB, rw);
		auth.addLens("TestProjC", pathC, rw);
		
		Path projectRoot = root.resolve("TestProj");
		Repo repo = new RepoImpl("TestProj", "", projectRoot, this);
		Repo repoA = new RepoImpl("TestProj", "A", projectRoot, this);
		Repo repoB = new RepoImpl("TestProj", "B", projectRoot, this);
		Repo repoC = new RepoImpl("TestProj", "C", projectRoot, this);
		repos.put(repo.getName(), repo);
		repos.put(repoA.getName(), repoA);
		repos.put(repoB.getName(), repoB);
		repos.put(repoC.getName(), repoC);
	}

	public static void main(String[] args) {
		new Thread(new GitLensServerImpl()).run();
	}

	@Override
	public String getUser() {
		return "koltai";
		// TODO z
	}

	@Override
	public Iterable<Repo> getRepos() {
		return repos.values();
	}

	@Override
	public Iterable<Repo> getRepos(String project) {
		ArrayList<Repo> out = new ArrayList<Repo>();
		for (Repo r : repos.values()) {
			if (r.getProject().equals(project)) {
				out.add(r);
			}
		}
		return out;
	}

	@Override
	public ColossalLens getColossalLens() {
		return colossalLens;
	}

	@Override
	public Auth getAuth() {
		return auth;
	}

	@Override
	public Repo getRepo(String repoName) {
		return repos.get(repoName);
	}
}
