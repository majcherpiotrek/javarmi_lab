package main;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfacesrmi.IManager;
import interfacesrmi.IRegistry;
import interfacesrmi.IWorker;
import interfacesrmi.iworker.impl.*;

public class Main {

	public static void main(String[] args) {
		
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry();
			IRegistry registry = (IRegistry) rmiRegistry.lookup("Registry");
			
			IManager manager = (IManager) registry.getManager();
			
			WorkerImpl worker = new WorkerImpl(manager);
			worker.setNumber(registry.registerObject(worker));
			System.out.println("WORKER: Worker number " + worker.getNumber() + " ready");
		} catch (RemoteException e) {
			System.err.println("WORKER: Worker error:\n");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("WORKER: Cannot find registry!:\n");
			e.printStackTrace();
		}

	}

}
