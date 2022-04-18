import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.SQLException;

public class Project2Driver
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException 
	{
		Project2GUI myFrame = new Project2GUI();
		myFrame.setVisible(true);
		myFrame.pack();
		myFrame.setLayout(new BorderLayout(2,0));
		
	}
}