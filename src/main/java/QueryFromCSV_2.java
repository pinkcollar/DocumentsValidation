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

public class QueryFromCSV_2 {
    public static void main(String[] args) {
        // Path to the CSV file
        String csvFilePath = "src/main/resources/Minor_PPM_Objects_Meter_Due.csv"; // Replace with the actual path to your CSV file

        // Database connection details
        String jdbcUrl = "jdbc:sqlserver://tlebtsdevsyncmbceam-ondemand.sql.azuresynapse.net:1433;database=cmbc_eam_replica;authentication=ActiveDirectoryInteractive;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.sql.azuresynapse.net;loginTimeout=30;";

        // Step 1: Read the CSV file and extract ppo_object and ppm_code values
        List<String[]> ppoObjectsAndPpmCodes = readPpoObjectsAndPpmCodesFromCSV(csvFilePath);

        if (ppoObjectsAndPpmCodes.isEmpty()) {
            System.out.println("No ppo_object or ppm_code values found in the CSV file.");
            return;
        }

        // Step 2: Construct the SQL query
        String query = constructQuery(ppoObjectsAndPpmCodes);

        // Step 3: Execute the query
        executeQuery(jdbcUrl, query);
    }

    /**
     * Reads the ppo_object and ppm_code columns from the CSV file.
     */
    private static List<String[]> readPpoObjectsAndPpmCodesFromCSV(String csvFilePath) {
        List<String[]> ppoObjectsAndPpmCodes = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> rows = reader.readAll();

            // Assuming the first row contains headers
            String[] headers = rows.get(0);
            int ppoObjectIndex = -1;
            int ppmCodeIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if ("ppo_object".equalsIgnoreCase(headers[i])) {
                    ppoObjectIndex = i;
                } else if ("\uFEFFppm_code".equalsIgnoreCase(headers[i])) {
                    ppmCodeIndex = i;
                }
            }

            if (ppoObjectIndex == -1 || ppmCodeIndex == -1) {
                throw new IllegalArgumentException("CSV file does not contain 'ppo_object' or 'ppm_code' columns.");
            }

            // Extract ppo_object and ppm_code values
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length > ppoObjectIndex && row.length > ppmCodeIndex) {
                    String ppoObject = row[ppoObjectIndex];
                    String ppmCode = row[ppmCodeIndex];
                    ppoObjectsAndPpmCodes.add(new String[]{ppoObject, ppmCode});
                }
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return ppoObjectsAndPpmCodes;
    }

    /**
     * Constructs the SQL query using the ppo_object and ppm_code values.
     */
    private static String constructQuery(List<String[]> ppoObjectsAndPpmCodes) {
        StringBuilder query = new StringBuilder("SELECT * FROM DS7USER.R5PPMOBJECTS WHERE ");

        for (int i = 0; i < ppoObjectsAndPpmCodes.size(); i++) {
            String[] ppoObjectAndPpmCode = ppoObjectsAndPpmCodes.get(i);
            query.append("(PPO_OBJECT = '").append(ppoObjectAndPpmCode[0]).append("' AND PPO_PPM = '").append(ppoObjectAndPpmCode[1]).append("')");
            if (i < ppoObjectsAndPpmCodes.size() - 1) {
                query.append(" OR ");
            }
        }

        query.append(";");
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
                        ", PPO_PPM: " + rs.getString("PPO_PPM") +
                        ", PPO_METER: " + rs.getString("PPO_METER"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}