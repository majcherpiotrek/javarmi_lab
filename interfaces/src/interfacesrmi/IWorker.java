package interfacesrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;

public interface IWorker extends Remote {
	
	boolean setTask(String task, Duration d, Object o) throws RemoteException;
	
	String getState() throws RemoteException;
	
	int getNumber() throws RemoteException;  
}
