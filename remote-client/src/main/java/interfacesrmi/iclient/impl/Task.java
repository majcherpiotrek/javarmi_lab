package interfacesrmi.iclient.impl;

public class Task {
	
	private String taskName;
	private long taskDuration;
	
	
	public Task(String taskName, long taskDuration) {
		super();
		this.taskName = taskName;
		this.taskDuration = taskDuration;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getTaskDuration() {
		return taskDuration;
	}
	public void setTaskDurationSeconds(long taskDuration) {
		this.taskDuration = taskDuration;
	}
}
