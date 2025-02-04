import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AzureSynapseConnection {
    public static void main(String[] args) {
        // JDBC URL with ActiveDirectoryInteractive authentication
        String jdbcUrl = "jdbc:sqlserver://tlebtsdevsyncmbceam-ondemand.sql.azuresynapse.net:1433;database=cmbc_eam_replica;authentication=ActiveDirectoryInteractive;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.sql.azuresynapse.net;loginTimeout=30;";

        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish the connection
            Connection conn = DriverManager.getConnection(jdbcUrl);

            // Create a statement object
            Statement stmt = conn.createStatement();

            // Execute a query
            ResultSet rs = stmt.executeQuery("SELECT * FROM DS7USER.R5PPMOBJECTS where PPO_OBJECT in ");  // Replace 'your_table' with your table name

            // Process the result set
           // while (rs.next()) {
                //System.out.println("Column1: " + rs.getString(1) + ", Column2: " + rs.getString(2));  // Adjust for your columns
            //}

            // Close the resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}