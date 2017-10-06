package interfacesrmi.imanager.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;

import interfacesrmi.IManager;

public class ManagerImpl extends UnicastRemoteObject implements IManager {

	protected ManagerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void assignTask(String task, Duration d, Object o) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() throws RemoteException {
		// TODO Auto-generated method stub

	}

}
