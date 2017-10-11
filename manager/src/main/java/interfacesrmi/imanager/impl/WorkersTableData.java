package interfacesrmi.imanager.impl;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class WorkersTableData extends AbstractTableModel {
	
	public class WorkerInformation {
		private int id;
		private int tasksNumber;
		private long sumaricTasksTimeSeconds;
		
		public WorkerInformation() {
			
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getTasksNumber() {
			return tasksNumber;
		}
		public void setTasksNumber(int tasksNumber) {
			this.tasksNumber = tasksNumber;
		}
		public long getSumaricTasksTimeSeconds() {
			return sumaricTasksTimeSeconds;
		}
		public void setSumaricTasksTimeSeconds(long sumaricTasksTimeSeconds) {
			this.sumaricTasksTimeSeconds = sumaricTasksTimeSeconds;
		}
		
		
	}
	
	private String[] columnNames;
	private List<WorkerInformation> workersList;
	
	public WorkersTableData(String[] columnNames) {
		this.columnNames = columnNames;
		workersList = new LinkedList<>();
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
		return workersList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		WorkerInformation worker = workersList.get(rowIndex);
		
		switch(columnIndex) {
		case 0: {
			return worker.getId();
		}
		case 1: {
			return worker.getTasksNumber();
		}
		case 2: {
			return worker.getSumaricTasksTimeSeconds();
		}
		default:
			return null;
		}
	}
	
	public void addWorkerInformation(List<WorkerInformation> workerInformationList) {
		this.workersList = workerInformationList;
		fireTableDataChanged();
	}
	
	
}
