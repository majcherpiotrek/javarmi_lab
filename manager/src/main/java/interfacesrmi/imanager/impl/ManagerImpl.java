package interfacesrmi.imanager.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import interfacesrmi.IManager;
import interfacesrmi.IRegistry;
import interfacesrmi.IWorker;

public class ManagerImpl extends UnicastRemoteObject implements IManager {
	
	private IRegistry registry;
	private List<IWorker> workersList;
	
	public ManagerImpl(IRegistry registry) throws RemoteException {
		super();
		this.registry = registry;
		this.workersList = (List<IWorker>)(List<?>) registry.getWorkers();
	}

	@Override
	public void assignTask(String task, Duration d, Object o) throws RemoteException {
		if (workersList.isEmpty()) {
			System.out.println("MANAGER: Cannot assign task because there are no workers!");
		} else {
			IWorker chosenWorker = chooseWorkerWithShortestSumaricTasksTime(workersList);
			chosenWorker.setTask(task, d, o);
			System.out.println("MANAGER: Assigned task \"" + task + "\" to worker number " + ((IWorker) chosenWorker).getNumber() );
		}
	}

	@Override
	public void refresh() throws RemoteException {
		workersList = (List<IWorker>)(List<?>) registry.getWorkers();
		System.out.println("MANAGER: refreshed workers list");
	}
	
	private long getWorkersTasksSumaricDurationInSeconds(IWorker worker) throws RemoteException {
		
		String workerState = worker.getState();
		int indexOfComma = workerState.indexOf(',');
		String workersTasksSumaricDurationSecondsString = workerState.substring(indexOfComma + 1, workerState.length());
		
		return Long.valueOf(workersTasksSumaricDurationSecondsString);
	}
	
	private IWorker chooseWorkerWithShortestSumaricTasksTime(List<IWorker> workersList) throws RemoteException {
		long shortestSumaricTasksTime = Long.MAX_VALUE;
		IWorker chosenWorker = null;
		for(IWorker worker : workersList) {
			long workersSumericTasksTime = getWorkersTasksSumaricDurationInSeconds(worker);
			if (workersSumericTasksTime < shortestSumaricTasksTime) {
				shortestSumaricTasksTime = workersSumericTasksTime;
				chosenWorker = worker;
			}
		}
		return chosenWorker;
	}
}
