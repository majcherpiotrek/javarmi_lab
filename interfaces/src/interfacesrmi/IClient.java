package interfacesrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	
	void setResult(String r) throws RemoteException;
}
