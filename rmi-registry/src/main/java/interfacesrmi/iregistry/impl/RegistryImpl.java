package interfacesrmi.iregistry.impl;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import interfacesrmi.*;

public class RegistryImpl extends UnicastRemoteObject implements IRegistry {
	
	private Map<Integer, IWorker> workersMap;
	private IManager manager;
	private int managerId;
	private Registry registry;
	
	public RegistryImpl(Registry rmiRegistry) throws RemoteException {
		super();
		workersMap = new HashMap<>();
		registry = rmiRegistry;
	}

	@Override
	public int registerObject(Object o) throws RemoteException {
		int newObjectId = generateNewObjectId();
		boolean objectAdded = false;
		if (IWorker.class.isInstance(o)) {
			workersMap.put(newObjectId, (IWorker) o);
			objectAdded = true;
			if (manager != null) {
				manager.refresh();
			}
		} else {
			if (IManager.class.isInstance(o)) {
				manager = (IManager) o;
				managerId = newObjectId;
				objectAdded = true;
			}
		}
		System.out.println("REGISTRY: " + (objectAdded ? ("registered new object with id " + newObjectId) : "could not register new object"));
		return objectAdded ? newObjectId : -1;
	}

	@Override
	public boolean unregisterObject(int number) throws RemoteException {
		boolean removedObject = false;
		if (number == managerId) {
			manager = null;
			removedObject = true;
		} else {
			for (Map.Entry<Integer, IWorker> worker : workersMap.entrySet()) {
				if (number == worker.getKey().intValue()) {
					workersMap.remove(worker.getKey());
					removedObject = true;
					break;
				}
			}
		}
		System.out.println("REGISTRY: " + (removedObject ? ("unregistered object with id " + number) : ("could not unregister the object with id " + number)));
		return removedObject;
	}

	@Override
	public Object getManager() throws RemoteException {
		return manager;
	}

	@Override
	public List<Object> getWorkers() throws RemoteException {
		List<Object> workersObjects = new LinkedList<>();
		workersObjects.addAll(workersMap.values());
		return workersObjects;
	}
	
	private Integer generateNewObjectId() {
		Integer generatedId;
		Random rand = new Random();
		boolean alreadyInTheMap;
		do {
			generatedId = rand.nextInt(Integer.MAX_VALUE);
			alreadyInTheMap = false;
			for(Map.Entry<Integer, IWorker> worker : workersMap.entrySet()) {
				if (managerId == generatedId.intValue() || worker.getKey().intValue() == generatedId.intValue()) {
					alreadyInTheMap = true;
					break;
				}
			}
		} while (alreadyInTheMap);
		
		return generatedId;
	}

}
