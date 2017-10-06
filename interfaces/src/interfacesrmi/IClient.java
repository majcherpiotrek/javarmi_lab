package interfacesrmi;

import java.rmi.Remote;

public interface IClient extends Remote {
	
	void setResult(String r);
}
