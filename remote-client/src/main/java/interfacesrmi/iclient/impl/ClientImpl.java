package interfacesrmi.iclient.impl;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interfacesrmi.IClient;
import interfacesrmi.IManager;
import interfacesrmi.IWorker;

public class ClientImpl extends UnicastRemoteObject implements IClient {
	
	private static final String APP_NAME = "RMI - Client";
	private static final String[] COLUMN_NAMES =  {"Task name", "Task duration"};
	
	private TaskTableData taskTableData;
	private List<Task> tasksList;
	private IManager manager;
	JTextArea textArea;
	
	public ClientImpl(IManager manager) throws RemoteException {
		super();
		this.taskTableData = new TaskTableData(COLUMN_NAMES);
		this.tasksList = new LinkedList<>();
		this.manager = manager;
	}

	@Override
	public void setResult(String r) throws RemoteException {
		textArea.append("CLIENT: Received result: " + r + "\n");
		System.out.println("CLIENT: Received result: " + r);
	}
	
	public void createAndShowGUI() {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame mainFrame = new JFrame(APP_NAME);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPane = new JPanel();
		
		// Setup labels and input fields
		JLabel taskNameLabel = new JLabel("Task name:");
		JLabel taskDurationLabel = new JLabel("Task duration:");
		
		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		JTextField taskNameInput = new JTextField(20);
		JFormattedTextField taskDurationInput = new JFormattedTextField(integerFormat);
		taskNameLabel.setLabelFor(taskNameInput);
		taskDurationLabel.setLabelFor(taskDurationInput);
		
		// Setup text area for responses
		textArea = new JTextArea(20, 40);
		textArea.setEditable(false);
		JScrollPane textAreaScrollPane = new JScrollPane(textArea);
		
		// Setup "Send task" button
		JButton sendTaskButton = new JButton("Send task!");
		sendTaskButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String taskName = taskNameInput.getText();
				Integer taskDuration = Integer.valueOf(taskDurationInput.getText());
				if (taskName == null || taskDuration == null) {
					return;
				}
				Duration d = Duration.ofSeconds(taskDuration);
				try {
					sendTask(taskName, d);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	    // Setup table
	    JTable tasksTable = new JTable(taskTableData);
		JScrollPane tableScrollPane = new JScrollPane(tasksTable);
		
		// Panel for labels
		JPanel labelPane = new JPanel(new GridLayout(0,1));
		labelPane.add(taskNameLabel);
		labelPane.add(taskDurationLabel);
				
		// Panel for input fields
		JPanel fieldPane = new JPanel(new GridLayout(0,1));
		fieldPane.add(taskNameInput);
		fieldPane.add(taskDurationInput);
				
		// Panel for the button
		JPanel buttonsPanel = new JPanel(new GridLayout(0, 1));
	    buttonsPanel.add(sendTaskButton);
	    
		// Main panel
		mainPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    mainPane.add(labelPane, BorderLayout.CENTER);
	    mainPane.add(fieldPane, BorderLayout.LINE_END);
	    mainPane.add(buttonsPanel);			    
		mainPane.add(tableScrollPane);
		mainPane.add(textAreaScrollPane);
	    mainFrame.getContentPane().add(mainPane);
	    mainFrame.pack();
	    mainFrame.setVisible(true);
	}
	
	private void sendTask(String taskName, Duration d) throws RemoteException {
		tasksList.add(new Task(taskName, d.getSeconds()));
		taskTableData.addTasks(tasksList);
		manager.assignTask(taskName, d, this);
	}
}
