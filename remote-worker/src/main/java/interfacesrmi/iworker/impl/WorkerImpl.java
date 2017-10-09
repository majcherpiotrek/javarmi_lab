package interfacesrmi.iworker.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import interfacesrmi.IClient;
import interfacesrmi.IManager;
import interfacesrmi.IWorker;

public class WorkerImpl extends UnicastRemoteObject implements IWorker {
	
	private class Task {
		
		private String taskName;
		private Duration duration;
		private IClient client;
		
		public Task(String taskName, Duration duration, IClient client) {
			this.taskName = taskName;
			this.duration = duration;
			this.client = client;
		}

		public String getTaskName() {
			return taskName;
		}

		public Duration getDuration() {
			return duration;
		}

		public IClient getClient() {
			return client;
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
		
		private Queue<Task> tasksQueue;
		private boolean stopRunning = false;
		private IManager manager;
		
		public WorkerRunnable(IManager manager) {
			this.tasksQueue = new ConcurrentLinkedQueue<>();
			this.manager = manager;
		}
		
		@Override
		public void run() {
			System.out.println("WORKER RUNNABLE: Starting...");
			
			while(!stopRunning) {
				
				Task currentTask = tasksQueue.peek();
				
				if (currentTask != null) {
					Duration taskDuration = currentTask.getDuration();
					long durationSeconds = taskDuration.getSeconds();
					
					System.out.println("WORKER RUNNABLE: Starting task: " + currentTask.getTaskName());
					for(long i = 0; i < durationSeconds; i++) {
						System.out.println(currentTask.getTaskName());
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
							
					try {
						currentTask.getClient().setResult("Task done: " + currentTask.getTaskName());
						this.manager.refresh();
					} catch (RemoteException e) {
						System.err.println("WORKER RUNNABLE: Could not set the task result to the client");
						e.printStackTrace();
					}								
				}
				
				tasksQueue.poll();
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		void stopWorker() {
			stopRunning = true;
		}

		public Queue<Task> getTasksQueue() {
			return tasksQueue;
		}
		
		public void putNewTask(Task task) {
			System.out.println("WORKER RUNNABLE: Received new task to put to the queue");
			tasksQueue.add(task);
			System.out.println("WORKER RUNNABLE: New task added");
		}
	}
	
	private int number;
	private WorkerRunnable workerRunnable;
	
	
	public WorkerImpl(IManager manager) throws RemoteException {
		super();
		workerRunnable = new WorkerRunnable(manager);
		Thread workerThread = new Thread(workerRunnable);
		workerThread.start();
	}

	@Override
	public boolean setTask(String task, Duration d, Object o) throws RemoteException {
		boolean taskAdded = false;
		if (task == null || d == null || !IClient.class.isInstance(o)) {
			System.err.println("Incorrect argument passed!");		
		} else {
			Task taskToAdd = new Task(task, d, (IClient) o);
			System.out.println("WORKER: Putting new task for the worker runnable");
			workerRunnable.putNewTask(taskToAdd);
			taskAdded = true;
		}
		
		System.out.println("WORKER: Task added!");
		return taskAdded;
	}

	@Override
	public String getState() throws RemoteException {
		int tasksNum = workerRunnable.getTasksQueue().size();
		long sumaricDuration = 0;
		for (Task task : workerRunnable.getTasksQueue()) {
			sumaricDuration += task.getDuration().getSeconds();
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
