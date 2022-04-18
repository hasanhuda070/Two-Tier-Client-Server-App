/*
 * Name: Showad Huda
 * Course: CNT4714 Summer 2021
 * Assignment title: “Project Two: Two-Tier Client-Server Application Development With MySQL and JDBC
 * Date: July 4, 2021
 * 
 * Project2GUI.java
*/

//Used WindowBuilder GUI org.eclipse.swt
//Pre-Processor Directives
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

//Import all java.sql library directives
import java.sql.*;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

//Class function
public class Project2GUI extends JFrame
{
	//JLabels for Driver, URL, Username (root and project2client), Password (root and P2client@), and Connection Status
	private JLabel jlbDriver;
	private JLabel jlbURL;
	private JLabel jlbUsername;
	private JLabel jlbPassword;
	private JLabel jlbConnection;
	//JComboBox for drop-down menu to select other JDBC Drivers and Database URLs if applicable 
	private JComboBox driverList;
	private JComboBox dataURL;
	//JFields for Username and Password
	private JTextField jtfUsername;
	private JPasswordField jpfPassword;
	//JTextArea for SQL commands input by user
	private JTextArea jtaSQLInput;
	//JButtons for connection, clearing, and executing. Use with mouse click
	private JButton jbtConnect;
	private JButton jbtClearSQL;
	private JButton jbtExecute;
	private JButton jbtClearWindow;
	//Table setup for display in window
	private ResultSetTableModel tableView = null;
	private JTable table;
	
	//Connection to database with statement to notify if connected or not
	private Connection connection;
	private boolean connectToDatabase = false;
	
	//Project2GUI throws if error occurs such as JDBC not referenced
	public Project2GUI() throws ClassNotFoundException, SQLException, IOException 
	{
		//Elements of GUI 
		this.elementsOfGUI();
		
		//Connect to Datahase action setup
		this.jbtConnect.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
				//JDBC selection from drop down selection
				try
				{
					Class.forName(String.valueOf(driverList.getSelectedItem()));
				} catch (ClassNotFoundException e) 
				{	
					//No Connection Now in red colored font
					jlbConnection.setText("No Connection Now");
					jlbConnection.setForeground(Color.RED);
					e.printStackTrace();
					//Table set to clear
					 table.setModel(new DefaultTableModel());
					 tableView = null;
				}
				
				//Database connection try
				try 
				{
					if(connectToDatabase == true)
					{
						connection.close();
						//No Connection Now in red colored font
						jlbConnection.setText("No Connection Now");
						jlbConnection.setForeground(Color.RED);
						connectToDatabase = false;
						//Table set to clear
						table.setModel(new DefaultTableModel());
						tableView = null;
					}
						
					connection = DriverManager.getConnection(String.valueOf(dataURL.getSelectedItem()), jtfUsername.getText(), jpfPassword.getText());
					//Reads URL and connects with font color to green
					jlbConnection.setText("Connected to " + String.valueOf(dataURL.getSelectedItem()));
					jlbConnection.setForeground(Color.GREEN);
					//Connection status updated
					connectToDatabase = true;
				} catch (SQLException e) 
				{
					jlbConnection.setText("No Connection Now");
					jlbConnection.setForeground(Color.RED);
					table.setModel(new DefaultTableModel());
					tableView = null;
					e.printStackTrace();
				}
				
			}
			
		});
		
		
		//SQL text set to clear
		this.jbtClearSQL.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				jtaSQLInput.setText("");
			}
			
		});
		
		//SQL execute command and table set
		this.jbtExecute.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
				//Table display for SQL results from executed statements
				if(connectToDatabase == true && tableView == null)
				{
					try 
					{
						//ResultSetTableModel.java is referred to to obtain format
						tableView = new ResultSetTableModel(jtaSQLInput.getText(), connection);
						table.setModel(tableView);
					} catch (ClassNotFoundException | SQLException | IOException e)
					{
						//Table set to clear
						 table.setModel(new DefaultTableModel());
						 tableView = null;
						//Warning pop up alert if errors occur
						 JOptionPane.showMessageDialog( null, 
			                        e.getMessage(), "Database Error", 
			                        JOptionPane.ERROR_MESSAGE );
						e.printStackTrace();
					}
				}
				else
				//Executer to table output
				if(connectToDatabase == true && tableView != null)
				{
					//Read if execute is query or update based
					String query = jtaSQLInput.getText(); 
					if(query.contains("select") || query.contains("SELECT"))
					{
						try 
						{
							tableView.setQuery(query);
						} catch (IllegalStateException | SQLException e) 
						{
							//Table set to clear
							 table.setModel(new DefaultTableModel());
							 tableView = null;
							//Warning pop up alert if errors occur
							 JOptionPane.showMessageDialog( null, 
				                        e.getMessage(), "Database error", 
				                        JOptionPane.ERROR_MESSAGE );
						
							e.printStackTrace();
						}
					}
					else
					{
						try 
						{
							tableView.setUpdate(query);
							//Table set to clear
							 table.setModel(new DefaultTableModel());
							 tableView = null;
						} catch (IllegalStateException | SQLException e) 
						{
							//Table set to clear
							 table.setModel(new DefaultTableModel());
							 tableView = null;
							//Warning pop up alert if errors occur
							 JOptionPane.showMessageDialog( null, 
				                        e.getMessage(), "Database error", 
				                        JOptionPane.ERROR_MESSAGE );
							 
							e.printStackTrace();
						}
					}
				}
				
			}
			
		});
		
		//clear table 
		this.jbtClearWindow.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//Table cleared
				table.setModel(new DefaultTableModel());
				tableView = null;
				
			}
			
		});

		//JPanel for all buttons
		JPanel buttons = new JPanel(new GridLayout(1, 4));
		buttons.add(this.jlbConnection);
		buttons.add(this.jbtConnect);
		buttons.add(this.jbtClearSQL);
		buttons.add(this.jbtExecute);

		//JPanel for labels and text
		JPanel labelsAndText = new JPanel(new GridLayout(4, 2));
		labelsAndText.add(this.jlbDriver);
		labelsAndText.add(this.driverList);
		labelsAndText.add(this.jlbURL);
		labelsAndText.add(this.dataURL);
		labelsAndText.add(this.jlbUsername);
		labelsAndText.add(this.jtfUsername);
		labelsAndText.add(this.jlbPassword);
		labelsAndText.add(this.jpfPassword);

		
		//JPanel for top area
		JPanel top = new JPanel(new GridLayout(1, 2));
		top.add(labelsAndText);
		top.add(this.jtaSQLInput);
		
		//JPanel for bottom area
		JPanel down = new JPanel();
		down.setLayout(new BorderLayout(20,0));
		JScrollPane scrollPane = new JScrollPane(this.table);
		scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		down.add(scrollPane, BorderLayout.CENTER);
		down.add(this.jbtClearWindow, BorderLayout.SOUTH);
		getContentPane().add(top, BorderLayout.NORTH);
		getContentPane().add(buttons, BorderLayout.CENTER);
		getContentPane().add(down, BorderLayout.SOUTH);
		
		
		  //Window closes at end of operation
	      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	      addWindowListener(new WindowAdapter() 
	         {
	            //Disconnect when exiting
	            public void windowClosed( WindowEvent event )
	            {
	            	try 
	            	{            		
						if(!connection.isClosed())
							connection.close();
					} catch (SQLException e) 
	            	{
						e.printStackTrace();
					}
	               System.exit( 0 );
	            }
	         });

	}
	
	//WindowBuilder elements of GUI for display. Buttons and text to give Windows XP view
	public void elementsOfGUI() throws ClassNotFoundException, SQLException, IOException 
	{
		String[] driverString = { "com.mysql.cj.jdbc.Driver", "" };
		String[] dataBaseURLString = { "jdbc:mysql://localhost:3306/project2", "" };

		// handle JLabel
		this.jlbDriver = new JLabel("JDBC Driver");
		jlbDriver.setHorizontalAlignment(SwingConstants.CENTER);
		jlbDriver.setForeground(new Color(0, 0, 0));
		jlbDriver.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.jlbURL = new JLabel("Database URL");
		jlbURL.setFont(new Font("Times New Roman", Font.BOLD, 12));
		jlbURL.setHorizontalAlignment(SwingConstants.CENTER);
		this.jlbUsername = new JLabel("Username");
		jlbUsername.setFont(new Font("Times New Roman", Font.BOLD, 12));
		jlbUsername.setHorizontalAlignment(SwingConstants.CENTER);
		this.jlbPassword = new JLabel("Password");
		jlbPassword.setFont(new Font("Times New Roman", Font.BOLD, 12));
		jlbPassword.setHorizontalAlignment(SwingConstants.CENTER);
		this.jlbConnection = new JLabel("No Connection Now");
		jlbConnection.setHorizontalAlignment(SwingConstants.CENTER);
		jlbConnection.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.jlbConnection.setForeground(new Color(255, 0, 0));

		// handle combo boxes
		this.driverList = new JComboBox(driverString);
		driverList.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		this.driverList.setSelectedIndex(0);
		this.dataURL = new JComboBox(dataBaseURLString);
		dataURL.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		// handle text fields
		this.jtfUsername = new JTextField();
		jtfUsername.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		this.jpfPassword = new JPasswordField();
		jpfPassword.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		// handle jtextarea
		this.jtaSQLInput = new JTextArea(5, 50);
		jtaSQLInput.setBackground(new Color(248, 248, 255));
		jtaSQLInput.setFont(new Font("Monospaced", Font.PLAIN, 14));
		this.jtaSQLInput.setWrapStyleWord(true);
		this.jtaSQLInput.setLineWrap(true);

		// handle buttons
		this.jbtConnect = new JButton("Connect to Database");
		jbtConnect.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.jbtClearSQL = new JButton("Clear SQL Command");
		jbtClearSQL.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.jbtExecute = new JButton("Execute SQL Command");
		jbtExecute.setFont(new Font("Times New Roman", Font.BOLD, 12));
		this.jbtClearWindow = new JButton("Clear Result Window");
		jbtClearWindow.setHorizontalAlignment(SwingConstants.LEFT);
		jbtClearWindow.setFont(new Font("Times New Roman", Font.BOLD, 12));
		
		//handle table
		this.table = new JTable();
	}
}
//END