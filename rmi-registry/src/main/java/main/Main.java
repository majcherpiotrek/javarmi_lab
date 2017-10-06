package main;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import interfacesrmi.IRegistry;
import interfacesrmi.iregistry.impl.RegistryImpl;

public class Main {

	public static void main(String[] args) {
		
		try {
			
			Registry rmiRegistry = LocateRegistry.createRegistry(1099);
			IRegistry myRegistryImpl = new RegistryImpl(rmiRegistry);
			System.out.println("RMI registry started:" + rmiRegistry.toString());
			
			rmiRegistry.bind("Registry", myRegistryImpl);
			System.out.println("Registry server ready");
			
		} catch (RemoteException e) {
			System.err.println("Registry server error:\n");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.err.println("Registry server error:\n");
			e.printStackTrace();
		}
	}

}
