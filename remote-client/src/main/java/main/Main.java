package main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;

import interfacesrmi.IClient;
import interfacesrmi.IManager;
import interfacesrmi.IRegistry;
import interfacesrmi.IWorker;
import interfacesrmi.iclient.impl.ClientImpl;

public class Main {

	public static void main(String[] args) {
		
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry();
			IRegistry registry = (IRegistry) rmiRegistry.lookup("Registry");
			System.out.println("CLIENT: registry found");
			
			IClient client = new ClientImpl();
			IManager manager = (IManager) registry.getManager();
			
			Duration d = Duration.ofSeconds(20);
			manager.assignTask(args[0], d, client);
		
			System.out.println("CLIENT: Assigned task to manager. Waiting for result.");
		} catch (RemoteException e) {
			System.err.println("CLIENT: Client error:\n");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("CLIENT: cannot find registry!:\n");
			e.printStackTrace();
		}
	}

}
