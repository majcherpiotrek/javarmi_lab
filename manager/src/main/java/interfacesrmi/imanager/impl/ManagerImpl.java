package interfacesrmi.imanager.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import interfacesrmi.IManager;
import interfacesrmi.IRegistry;
import interfacesrmi.IWorker;
import main.WorkersTableData;
import main.WorkersTableData.WorkerInformation;

public class ManagerImpl extends UnicastRemoteObject implements IManager {
	
	private static final String APP_NAME = "RMI - Manager";
	private static final String[] COLUMN_NAMES =  {"Worker's Id", "Total tasks number", "Total tasks time"};
	private IRegistry registry;
	private List<IWorker> workersList;
	private WorkersTableData workersTableData;
	
	public ManagerImpl(IRegistry registry) throws RemoteException {
		super();
		this.registry = registry;
		this.workersList = (List<IWorker>)(List<?>) registry.getWorkers();
		this.workersTableData = new WorkersTableData(COLUMN_NAMES);
	}

	@Override
	public void assignTask(String task, Duration d, Object o) throws RemoteException {
		if (workersList.isEmpty()) {
			System.out.println("MANAGER: Cannot assign task because there are no workers!");
		} else {
			IWorker chosenWorker = chooseWorkerWithShortestSumaricTasksTime(workersList);
			chosenWorker.setTask(task, d, o);
			System.out.println("MANAGER: Assigned task \"" + task + " to worker number " + ((IWorker) chosenWorker).getNumber() );
			this.refresh();
		}
	}

	@Override
	public void refresh() throws RemoteException {
		workersList = (List<IWorker>)(List<?>) registry.getWorkers();
		System.out.println("MANAGER: refreshed workers list");
		workersTableData.addWorkerInformation(getUpdatedWorkerInformationList());
	}
	
	private long getWorkerTasksSumaricDurationInSeconds(IWorker worker) throws RemoteException {
		
		String workerState = worker.getState();
		int indexOfComma = workerState.indexOf(',');
		String workersTasksSumaricDurationSecondsString = workerState.substring(indexOfComma + 1, workerState.length());
		
		return Long.valueOf(workersTasksSumaricDurationSecondsString);
	}
	
	private int getWorkerTasksNumber(IWorker worker) throws RemoteException {
		String workerState = worker.getState();
		int indexOfComma = workerState.indexOf(',');
		String tasksNumString = workerState.substring(0, indexOfComma);
		return Integer.valueOf(tasksNumString);
	}
	
	private IWorker chooseWorkerWithShortestSumaricTasksTime(List<IWorker> workersList) throws RemoteException {
		long shortestSumaricTasksTime = Long.MAX_VALUE;
		IWorker chosenWorker = null;
		for(IWorker worker : workersList) {
			long workersSumericTasksTime = getWorkerTasksSumaricDurationInSeconds(worker);
			if (workersSumericTasksTime < shortestSumaricTasksTime) {
				shortestSumaricTasksTime = workersSumericTasksTime;
				chosenWorker = worker;
			}
		}
		return chosenWorker;
	}
	
	public void createAndShowGUI() throws RemoteException {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame mainFrame = new JFrame(APP_NAME);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel workersLabel = new JLabel("Workers");
		
		workersTableData.addWorkerInformation(getUpdatedWorkerInformationList());
		JTable workersTable = new JTable(workersTableData);
		JScrollPane scrollPane = new JScrollPane(workersTable);
		
		mainFrame.getContentPane().add(workersLabel);
		mainFrame.getContentPane().add(scrollPane);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private List<WorkerInformation> getUpdatedWorkerInformationList() throws RemoteException {
		List<WorkerInformation> workerInformationList = new LinkedList<>();
		for (IWorker worker : workersList) {
			WorkerInformation workerInformation = workersTableData.new WorkerInformation();
			workerInformation.setId(worker.getNumber());
			workerInformation.setTasksNumber(getWorkerTasksNumber(worker));
			workerInformation.setSumaricTasksTimeSeconds(getWorkerTasksSumaricDurationInSeconds(worker));
			
			workerInformationList.add(workerInformation);
		}
		return workerInformationList;
	}
}
