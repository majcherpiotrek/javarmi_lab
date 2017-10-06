package interfacesrmi.iclient.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfacesrmi.IClient;

public class ClientImpl extends UnicastRemoteObject implements IClient {
	
	private String result;
	
	public ClientImpl() throws RemoteException {
		super();
	}

	@Override
	public void setResult(String r) throws RemoteException {
		result = r;
		System.out.println("CLIENT: Received result: " + result);
	}

}
