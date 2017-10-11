package interfacesrmi.iclient.impl;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class TaskTableData extends AbstractTableModel {
	
	private String[] columnNames;
	private List<Task> taskList;
	
	public TaskTableData(String[] columnNames) {
		this.columnNames = columnNames;
		taskList = new LinkedList<>();
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return taskList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Task task = taskList.get(rowIndex);
		
		switch(columnIndex) {
		case 0: {
			return task.getTaskName();
		}
		case 1: {
			return task.getTaskDuration();
		}
		default:
			return null;
		}
	}
	
	public void addTasks(List<Task> taskList) {
		this.taskList = taskList;
		fireTableDataChanged();
	}
}
