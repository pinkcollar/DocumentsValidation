import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareSQLAndJSONResults {

    public static void main(String[] args) {
        // Path to the CSV file
        String csvFilePath = "src/main/resources/Minor_PPM_Objects_Meter_Due.csv"; // Replace with the actual path to your CSV file

        // Database connection details
        String jdbcUrl = "jdbc:sqlserver://tlebtsdevsyncmbceam-ondemand.sql.azuresynapse.net:1433;database=cmbc_eam_replica;authentication=ActiveDirectoryInteractive;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.sql.azuresynapse.net;loginTimeout=30;";

        // Read the CSV file and extract ppo_object and ppm_code values
        System.out.println("Reading CSV file...");
        List<String[]> ppoObjectsAndPpmCodes = readPpoObjectsAndPpmCodesFromCSV(csvFilePath);

        if (ppoObjectsAndPpmCodes.isEmpty()) {
            System.out.println("No ppo_object or ppm_code values found in the CSV file.");
            return;
        }
        System.out.println("CSV file read successfully. Retrieved " + ppoObjectsAndPpmCodes.size() + " records.");

        // Process records in batches of 10
        int batchSize = 10;
        for (int i = 0; i < ppoObjectsAndPpmCodes.size(); i += batchSize) {
            int end = Math.min(i + batchSize, ppoObjectsAndPpmCodes.size());
            List<String[]> batch = ppoObjectsAndPpmCodes.subList(i, end);

            System.out.println("\nProcessing batch " + (i / batchSize + 1) + " (Records " + (i + 1) + " to " + end + ")");

            // Step 1: Execute SQL query and retrieve results for the batch
            System.out.println("Executing SQL queries for the batch...");
            Map<String, Map<String, String>> sqlResults = executeSQLQuery(jdbcUrl, batch);
            System.out.println("SQL queries executed successfully. Retrieved " + sqlResults.size() + " results.");

            // Step 2: Send POST requests and retrieve JSON responses for the batch
            System.out.println("Retrieving JSON objects for the batch...");
            Map<String, Map<String, String>> jsonResults = retrieveJSONResponses(batch);
            System.out.println("JSON objects retrieved successfully. Retrieved " + jsonResults.size() + " results.");

            // Step 3: Compare SQL and JSON results for the batch
            System.out.println("Comparing SQL and JSON results for the batch...");
            compareResults(sqlResults, jsonResults);
            System.out.println("Comparison completed for the batch.");
        }
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
                } else if ("ppm_code".equalsIgnoreCase(headers[i])) {
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
     * Executes the SQL query and retrieves results.
     */
    private static Map<String, Map<String, String>> executeSQLQuery(String jdbcUrl, List<String[]> ppoObjectsAndPpmCodes) {
        Map<String, Map<String, String>> sqlResults = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {

            for (String[] ppoObjectAndPpmCode : ppoObjectsAndPpmCodes) {
                String ppoObject = ppoObjectAndPpmCode[0];
                String ppmCode = ppoObjectAndPpmCode[1];

                System.out.println("Executing SQL query for PPO_OBJECT: " + ppoObject + ", PPO_PPM: " + ppmCode);
                String query = "SELECT PPO_OBJECT, PPO_PPM, PPO_METER FROM DS7USER.R5PPMOBJECTS WHERE PPO_OBJECT = '" + ppoObject + "' AND PPO_PPM = '" + ppmCode + "'";
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        String sqlPpoObject = rs.getString("PPO_OBJECT");
                        String sqlPpoPpm = rs.getString("PPO_PPM");
                        String sqlPpoMeter = rs.getString("PPO_METER");

                        // Store SQL results in a map
                        Map<String, String> result = new HashMap<>();
                        result.put("PPO_OBJECT", sqlPpoObject);
                        result.put("PPO_PPM", sqlPpoPpm);
                        result.put("PPO_METER", sqlPpoMeter);

                        sqlResults.put(sqlPpoObject + "|" + sqlPpoPpm, result);
                        System.out.println("Query retrieved for PPO_OBJECT: " + sqlPpoObject + ", PPO_PPM: " + sqlPpoPpm);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sqlResults;
    }

    /**
     * Sends POST requests and retrieves JSON responses.
     */
    private static Map<String, Map<String, String>> retrieveJSONResponses(List<String[]> ppoObjectsAndPpmCodes) {
        Map<String, Map<String, String>> jsonResults = new HashMap<>();

        for (String[] ppoObjectAndPpmCode : ppoObjectsAndPpmCodes) {
            String ppoObject = ppoObjectAndPpmCode[0];
            String ppmCode = ppoObjectAndPpmCode[1];

            System.out.println("Retrieving JSON object for PPO_OBJECT: " + ppoObject + ", PPO_PPM: " + ppmCode);

            // Construct the JSON body dynamically
            String jsonBody = "{"
                    + "\"GRID_TYPE\": {"
                    + "\"TYPE\": \"LIST\""
                    + "},"
                    + "\"GRID\": {"
                    + "\"CURRENT_TAB_NAME\": \"LST\","
                    + "\"GRID_NAME\": \"1UTPPO\","
                    + "\"NUMBER_OF_ROWS_FIRST_RETURNED\": 10,"
                    + "\"CURSOR_POSITION\": 0"
                    + "},"
                    + "\"ADDON_FILTER\": {"
                    + "\"ALIAS_NAME\": \"PPO_OBJECT\","
                    + "\"OPERATOR\": \"=\","
                    + "\"VALUE\": \"" + ppoObject + "\""
                    + "},"
                    + "\"MULTIADDON_FILTERS\": {"
                    + "\"MADDON_FILTER\": ["
                    + "{"
                    + "\"ALIAS_NAME\": \"PPO_PPM\","
                    + "\"OPERATOR\": \"=\","
                    + "\"VALUE\": \"" + ppmCode + "\""
                    + "}"
                    + "]"
                    + "},"
                    + "\"REQUEST_TYPE\": \"LIST.HEAD_DATA.STORED\""
                    + "}";

            // Send the POST request and parse the JSON response
            String jsonResponse = sendPostRequest(jsonBody);
            if (jsonResponse != null) {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject resultData = jsonObject.getJSONObject("Result").getJSONObject("ResultData");
                JSONArray dataRecords = resultData.getJSONArray("DATARECORD");

                for (int i = 0; i < dataRecords.length(); i++) {
                    JSONObject dataRecord = dataRecords.getJSONObject(i);
                    JSONArray dataFields = dataRecord.getJSONArray("DATAFIELD");

                    String jsonPpoObject = "";
                    String jsonPpoPpm = "";
                    String jsonPpoMeter = "";

                    for (int j = 0; j < dataFields.length(); j++) {
                        JSONObject dataField = dataFields.getJSONObject(j);
                        String fieldName = dataField.getString("FIELDNAME");

                        switch (fieldName) {
                            case "ppo_object":
                                jsonPpoObject = dataField.getString("FIELDVALUE");
                                break;
                            case "ppo_ppm":
                                jsonPpoPpm = dataField.getString("FIELDVALUE");
                                break;
                            case "ppo_meter":
                                jsonPpoMeter = dataField.getString("FIELDVALUE");
                                break;
                        }
                    }

                    // Store JSON results in a map
                    Map<String, String> result = new HashMap<>();
                    result.put("PPO_OBJECT", jsonPpoObject);
                    result.put("PPO_PPM", jsonPpoPpm);
                    result.put("PPO_METER", jsonPpoMeter);

                    jsonResults.put(jsonPpoObject + "|" + jsonPpoPpm, result);
                    System.out.println("JSON object retrieved for PPO_OBJECT: " + jsonPpoObject + ", PPO_PPM: " + jsonPpoPpm);
                }
            }
        }

        return jsonResults;
    }

    /**
     * Sends a POST request with the given JSON body and returns the response.
     */
    private static String sendPostRequest(String jsonBody) {
        // URL for the POST request
        String url = "https://ca1.eam.hxgnsmartcloud.com:443/axis/restservices/grids";

        // Headers for the POST request
        String tenantHeader = "BVBWV1707339191_DEV";
        String organizationHeader = "*";
        String apiKey = "se13180466-50d9-4272-9fdd-e209a822f905";

        try {
            // Create the URL object
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set the request headers
            connection.setRequestProperty("Tenant", tenantHeader);
            connection.setRequestProperty("Organization", organizationHeader);
            connection.setRequestProperty("X-Api-Key", apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Enable output and input
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Write the JSON body to the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                // Handle error response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    System.out.println("Error Response: " + errorResponse.toString());
                }
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Compares SQL and JSON results.
     */
    private static void compareResults(Map<String, Map<String, String>> sqlResults, Map<String, Map<String, String>> jsonResults) {
        for (String key : sqlResults.keySet()) {
            Map<String, String> sqlResult = sqlResults.get(key);
            Map<String, String> jsonResult = jsonResults.get(key);

            if (jsonResult == null) {
                System.out.println("No matching JSON result for SQL key: " + key);
                continue;
            }

            // Compare PPO_OBJECT, PPO_PPM, and PPO_METER
            boolean isMatch = sqlResult.get("PPO_OBJECT").equals(jsonResult.get("PPO_OBJECT")) &&
                    sqlResult.get("PPO_PPM").equals(jsonResult.get("PPO_PPM")) &&
                    sqlResult.get("PPO_METER").equals(jsonResult.get("PPO_METER"));

            if (isMatch) {
                System.out.println("Match found for key: " + key);
            } else {
                System.out.println("Mismatch found for key: " + key);
                System.out.println("SQL Result: " + sqlResult);
                System.out.println("JSON Result: " + jsonResult);
            }
        }
    }
}