package interfacesrmi.iworker.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import interfacesrmi.IClient;
import interfacesrmi.IManager;
import interfacesrmi.IWorker;

public class WorkerImpl extends UnicastRemoteObject implements IWorker {
	
	private class Task {
		
		private String taskName;
		private Duration duration;
		
		public Task(String task, Duration d) {
			taskName = task;
			duration = d;
		}

		public String getTaskName() {
			return taskName;
		}

		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}

		public Duration getDuration() {
			return duration;
		}

		public void setDuration(Duration duration) {
			this.duration = duration;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((duration == null) ? 0 : duration.hashCode());
			result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Task other = (Task) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (duration == null) {
				if (other.duration != null)
					return false;
			} else if (!duration.equals(other.duration))
				return false;
			if (taskName == null) {
				if (other.taskName != null)
					return false;
			} else if (!taskName.equals(other.taskName))
				return false;
			return true;
		}

		private WorkerImpl getOuterType() {
			return WorkerImpl.this;
		}
		
		
	}
	
	private class WorkerRunnable implements Runnable{
		
		private Map<Task, IClient> tasksMap;
		private boolean stopRunning = false;
		private IManager manager;
		
		public WorkerRunnable(Map<Task, IClient> tasksMap, IManager manager) {
			this.tasksMap = tasksMap;
			this.manager = manager;
		}
		
		@Override
		public void run() {
			System.out.println("WORKER RUNNABLE: Starting...");
			while(!stopRunning) {
				if (!tasksMap.isEmpty()) {
					
					synchronized(tasksMap) {
						for (Iterator<Map.Entry<Task, IClient>> it = tasksMap.entrySet().iterator(); it.hasNext(); ) {
							Map.Entry<Task, IClient> taskEntry = it.next();
							Duration taskDuration = taskEntry.getKey().getDuration();
							long durationSeconds = taskDuration.getSeconds();
							
							System.out.println("WORKER RUNNABLE: Starting task: " + taskEntry.getKey().getTaskName());
							for(long i = 0; i < durationSeconds; i++) {
								System.out.println(taskEntry.getKey().getTaskName());
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
							try {
								taskEntry.getValue().setResult("Task done: " + taskEntry.getKey().getTaskName());
							} catch (RemoteException e) {
								System.err.println("WORKER RUNNABLE: Could not set the task result to the client");
								e.printStackTrace();
							}
							
							it.remove();
						}
					}
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		void stopWorker() {
			stopRunning = true;
		}

		public Map<Task, IClient> getTasksMap() {
			return tasksMap;
		}
		
		public void putNewTask(Task task, IClient client) {
			System.out.println("WORKER RUNNABLE: Received new task to put to the queue");
			synchronized(tasksMap) {
				tasksMap.put(task, client);
			}
			System.out.println("WORKER RUNNABLE: New task added");
		}
	}
	
	private int number;
	private WorkerRunnable workerRunnable;
	
	
	public WorkerImpl(IManager manager) throws RemoteException {
		super();
		workerRunnable = new WorkerRunnable(new LinkedHashMap<>(), manager);
		Thread workerThread = new Thread(workerRunnable);
		workerThread.start();
	}

	@Override
	public boolean setTask(String task, Duration d, Object o) throws RemoteException {
		boolean taskAdded = false;
		if (task == null || d == null || !IClient.class.isInstance(o)) {
			System.err.println("Incorrect argument passed!");		
		} else {
			Task taskToAdd = new Task(task, d);
			System.out.println("WORKER: Putting new task for the worker runnable");
			workerRunnable.putNewTask(taskToAdd, (IClient) o);
			taskAdded = true;
		}
		
		System.out.println("WORKER: Task added!");
		return taskAdded;
	}

	@Override
	public String getState() throws RemoteException {
		int tasksNum = workerRunnable.getTasksMap().size();
		long sumaricDuration = 0;
		for (Map.Entry<Task, IClient> task : workerRunnable.getTasksMap().entrySet()) {
			sumaricDuration += task.getKey().getDuration().getSeconds();
		}
		String workerState = Integer.toString(tasksNum) + "," + Long.toString(sumaricDuration);
		return workerState;
	}

	@Override
	public int getNumber() throws RemoteException {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
}
