package interfacesrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;

public interface IManager extends Remote {
	
	void assignTask(String task, Duration d, Object o) throws RemoteException;
	
	void refresh() throws RemoteException; 
}
