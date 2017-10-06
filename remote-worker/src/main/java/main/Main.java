package main;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfacesrmi.IRegistry;
import interfacesrmi.IWorker;
import interfacesrmi.iworker.impl.*;

public class Main {

	public static void main(String[] args) {
		
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry();
			IRegistry registry = (IRegistry) rmiRegistry.lookup("Registry");
			
			IWorker worker = new WorkerImpl();
			registry.registerObject(worker);
			System.out.println("Worker ready");
		} catch (RemoteException e) {
			System.err.println("Worker error:\n");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("Worker cannot find registry!:\n");
			e.printStackTrace();
		}

	}

}
