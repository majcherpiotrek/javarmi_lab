package interfacesrmi.iworker.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import interfacesrmi.IClient;
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

	private Map<Task, IClient> tasksMap;
	private int number;
	
	
	public WorkerImpl() throws RemoteException {
		super();
		tasksMap = new LinkedHashMap<>();
	}

	@Override
	public boolean setTask(String task, Duration d, Object o) throws RemoteException {
		boolean taskAdded = false;
		if (task == null || d == null || !IClient.class.isInstance(o)) {
			System.err.println("Incorrect argument passed!");		
		} else {
			Task taskToAdd = new Task(task, d);
			tasksMap.put(taskToAdd, (IClient) o);
			taskAdded = true;
		}
		return taskAdded;
	}

	@Override
	public String getState() throws RemoteException {
		int tasksNum = tasksMap.size();
		long sumaricDuration = 0;
		for (Map.Entry<Task, IClient> task : tasksMap.entrySet()) {
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
