import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class EstablishingConnectionUsingProperties {
   public static void main(String args[]) throws SQLException {
      //Registering the Driver
      System.setProperty("Jdbc.drivers", "com.mysql.jdbc.Driver");
      Properties properties = new Properties();
      properties.put("user", "root");
      properties.put("password", "root");
      //Getting the connection
      String url = "jdbc:mysql://localhost/operationslog";
      Connection con = DriverManager.getConnection(url, properties);
      System.out.println("Connection established: "+ con);
   }
}