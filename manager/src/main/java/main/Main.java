package main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfacesrmi.IRegistry;
import interfacesrmi.imanager.impl.ManagerImpl;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry();
			IRegistry registry = (IRegistry) rmiRegistry.lookup("Registry");
			
			ManagerImpl manager = new ManagerImpl(registry);
			int number = registry.registerObject(manager);
			System.out.println("MANAGER: Manager registered with number " + number);
			manager.createAndShowGUI();
		} catch (RemoteException e) {
			System.err.println("MANAGER: Manager error:\n");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("MANAGER: Cannot find registry!:\n");
			e.printStackTrace();
		}

	}
}
