import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryFromCSV {
    public static void main(String[] args) {
        // Path to the CSV file
        String csvFilePath = "src/main/resources/Minor_PPM_Objects_Meter_Due.csv"; // Replace with the actual path to your CSV file

        // Database connection details
        String jdbcUrl = "jdbc:sqlserver://tlebtsdevsyncmbceam-ondemand.sql.azuresynapse.net:1433;database=cmbc_eam_replica;authentication=ActiveDirectoryInteractive;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.sql.azuresynapse.net;loginTimeout=30;";

        // Step 1: Read the CSV file and extract ppo_object values
        List<String> ppoObjects = readPpoObjectsFromCSV(csvFilePath);

        if (ppoObjects.isEmpty()) {
            System.out.println("No ppo_object values found in the CSV file.");
            return;
        }

        // Step 2: Construct the SQL query
        String query = constructQuery(ppoObjects);

        // Step 3: Execute the query
        executeQuery(jdbcUrl, query);
    }

    /**
     * Reads the ppo_object column from the CSV file.
     */
    private static List<String> readPpoObjectsFromCSV(String csvFilePath) {
        List<String> ppoObjects = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> rows = reader.readAll();

            // Assuming the first row contains headers and ppo_object is the column name
            int ppoObjectIndex = -1;
            String[] headers = rows.get(0);
            for (int i = 0; i < headers.length; i++) {
                if ("ppo_object".equalsIgnoreCase(headers[i])) {
                    ppoObjectIndex = i;
                    break;
                }
            }

            if (ppoObjectIndex == -1) {
                throw new IllegalArgumentException("CSV file does not contain a 'ppo_object' column.");
            }

            // Extract ppo_object values
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length > ppoObjectIndex) {
                    ppoObjects.add(row[ppoObjectIndex]);
                }
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return ppoObjects;
    }

    /**
     * Constructs the SQL query using the ppo_object values.
     */
    private static String constructQuery(List<String> ppoObjects) {
        StringBuilder query = new StringBuilder("SELECT * FROM DS7USER.R5PPMOBJECTS WHERE PPO_OBJECT IN (");

        for (int i = 0; i < ppoObjects.size(); i++) {
            query.append("'").append(ppoObjects.get(i)).append("'");
            if (i < ppoObjects.size() - 1) {
                query.append(", ");
            }
        }

        query.append(");");
        return query.toString();
    }

    /**
     * Executes the SQL query against the database.
     */
    private static void executeQuery(String jdbcUrl, String query) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Process the result set
            while (rs.next()) {
                // Adjust the column names and types as per your table structure
                System.out.println("PPO_OBJECT: " + rs.getString("PPO_OBJECT") +
                        ", PPO_METER: " + rs.getString("PPO_METER"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}