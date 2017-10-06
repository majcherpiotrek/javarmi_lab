package interfacesrmi.iclient.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfacesrmi.IClient;

public class ClientImpl extends UnicastRemoteObject implements IClient {

	protected ClientImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setResult(String r) {
		// TODO Auto-generated method stub

	}

}
