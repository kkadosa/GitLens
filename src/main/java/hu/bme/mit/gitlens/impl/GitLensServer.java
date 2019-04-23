package hu.bme.mit.gitlens.impl;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import hu.bme.mit.gitlens.GitLensService;

public class GitLensServer implements Runnable {
	private static final int PORT = 12147;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
        try {
        	//TODO:initialization
        		TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);
        		GitLensServiceImpl service = new GitLensServiceImpl();        		
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

	public static void main(String[] args) {
        new Thread(new GitLensServer()).run();
	}
}
