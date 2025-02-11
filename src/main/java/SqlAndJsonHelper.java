import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import java.sql.DriverManager;

public class SqlAndJsonHelper {

    // API URL and headers
    private static final String API_URL = "https://ca1.eam.hxgnsmartcloud.com:443/axis/restservices/grids";
    private static final String TENANT_HEADER = "BVBWV1707339191_DEV";
    private static final String ORGANIZATION_HEADER = "*";
    private static final String API_KEY = "se13180466-50d9-4272-9fdd-e209a822f905";

    // Database connection details
    private static final String JDBC_URL = "jdbc:sqlserver://tlebtsdevsyncmbceam-ondemand.sql.azuresynapse.net:1433;"
            + "database=cmbc_eam_replica;"
            + "authentication=ActiveDirectoryInteractive;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "hostNameInCertificate=*.sql.azuresynapse.net;"
            + "loginTimeout=30;";

    public static void main(String[] args) {
        SqlAndJsonHelper helper = new SqlAndJsonHelper();

        String gridName = "1UTREA"; // Example grid
        String aliasName = "rea_lastsaved";
        String filterValueStart = "02/06/2025 15:50"; // Example filter value start
        String filterValueEnd = "02/07/2025 15:50"; // Example filter value end

        // Fetch JSON records
        List<Map<String, String>> jsonRecords = helper.getJSONRecords(gridName, aliasName, filterValueStart, filterValueEnd);
        System.out.println("Retrieved JSON Records: " + jsonRecords.size());

        // Fetch SQL records
        List<Map<String, String>> sqlRecords = helper.getSQLRecords(filterValueStart, filterValueEnd);
        System.out.println("Retrieved SQL Records: " + sqlRecords.size());

        // Compare records
        JSONSQLComparator.compareRecords(jsonRecords, sqlRecords);
    }

    /**
     * Establishes a connection to Azure Synapse Analytics.
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves SQL records that match the filter criteria.
     */
    public List<Map<String, String>> getSQLRecords(String filterValueStart, String filterValueEnd) {
        List<Map<String, String>> records = new ArrayList<>();
        String sqlQuery = "SELECT rea_code, rea_diff, rea_meter, rea_object, rea_reading FROM [DS7USER].[R5READINGS] WHERE REA_LASTSAVED >= ? AND REA_LASTSAVED < ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setString(1, filterValueStart);
            stmt.setString(2, filterValueEnd);
            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);

                    // Convert nulls to empty strings for comparison
                    if (value == null) {
                        value = "";
                    }

                    // Remove trailing zeros from floating-point numbers
                    if (columnName.equals("rea_diff") || columnName.equals("rea_reading")) {
                        value = value.replaceAll("\\.0+$", "").replaceAll("(\\.[0-9]*[1-9])0+$", "$1");
                    }

                    row.put(columnName, value);
                }
                records.add(row);
            }
        } catch (SQLException e) {
            System.err.println("SQL Query Failed: " + e.getMessage());
        }
        return records;
    }

    /**
     * Retrieves JSON records from the API that match the filter criteria.
     */
    public List<Map<String, String>> getJSONRecords(String gridName, String aliasName, String filterValueStart, String filterValueEnd) {
        List<Map<String, String>> records = new ArrayList<>();
        int cursorPosition = 0;
        int batchNumber = 1;

        while (true) {
            // Construct the JSON body with the current cursor position
            String jsonBody = "{"
                    + "\"GRID_TYPE\": {\"TYPE\": \"LIST\"},"
                    + "\"GRID\": {"
                    + "\"CURRENT_TAB_NAME\": \"LST\","
                    + "\"GRID_NAME\": \"" + gridName + "\","
                    + "\"NUMBER_OF_ROWS_FIRST_RETURNED\": 10000,"
                    + "\"CURSOR_POSITION\": " + cursorPosition
                    + "},"
                    + "\"MULTIADDON_FILTERS\": {"
                    + "\"MADDON_FILTER\": [{"
                    + "\"ALIAS_NAME\": \"" + aliasName + "\","
                    + "\"OPERATOR\": \">=\","
                    + "\"VALUE\": \"" + filterValueStart + "\","
                    + "\"JOINER\": \"AND\","
                    + "\"SEQNUM\": 1"
                    + "}, {"
                    + "\"ALIAS_NAME\": \"" + aliasName + "\","
                    + "\"OPERATOR\": \"<\","
                    + "\"VALUE\": \"" + filterValueEnd + "\","
                    + "\"JOINER\": \"AND\","
                    + "\"SEQNUM\": 2"
                    + "}]"
                    + "},"
                    + "\"REQUEST_TYPE\": \"LIST.HEAD_DATA.STORED\""
                    + "}";

            System.out.println("Retrieving JSON data for batch #" + batchNumber + " with cursor position: " + cursorPosition);

            // Send the POST request and parse the JSON response
            String jsonResponse = sendPostRequest(jsonBody);
            if (jsonResponse == null) {
                System.out.println("Failed to retrieve JSON data for batch #" + batchNumber);
                break; // Stop if there's an error in the request
            }

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject resultData = jsonObject.getJSONObject("Result").getJSONObject("ResultData");

            // Check if DATARECORD is null (no records)
            if (resultData.isNull("DATARECORD")) {
                System.out.println("No more records to fetch. Pagination complete.");
                break; // No more records to fetch
            }

            JSONArray dataRecords = resultData.getJSONArray("DATARECORD");
            for (int i = 0; i < dataRecords.length(); i++) {
                JSONObject dataRecord = dataRecords.getJSONObject(i);
                JSONArray dataFields = dataRecord.getJSONArray("DATAFIELD");
                Map<String, String> record = new HashMap<>();
                for (int j = 0; j < dataFields.length(); j++) {
                    JSONObject dataField = dataFields.getJSONObject(j);
                    String fieldName = dataField.getString("FIELDNAME");
                    String fieldValue = dataField.getString("FIELDVALUE");

                    // Convert empty strings to nulls for comparison
                    if (fieldValue.isEmpty()) {
                        fieldValue = "";
                    }

                    // Remove commas from numeric values
                    if (fieldName.equals("rea_diff") || fieldName.equals("rea_reading")) {
                        fieldValue = fieldValue.replace(",", "");
                    }

                    // Remove trailing zeros from floating-point numbers
                    if (fieldName.equals("rea_diff") || fieldName.equals("rea_reading")) {
                        fieldValue = fieldValue.replaceAll("\\.0+$", "").replaceAll("(\\.[0-9]*[1-9])0+$", "$1");
                    }

                    record.put(fieldName, fieldValue);
                }
                records.add(record);
            }

            System.out.println("Retrieved " + dataRecords.length() + " records in batch #" + batchNumber);
            System.out.println("Cumulative total records: " + records.size());

            // Check if there are more records to fetch
            if (resultData.isNull("NEXTCURSORPOSITION")) {
                System.out.println("No more records to fetch. Pagination complete.");
                break; // No more records to fetch
            }

            int nextCursorPosition = resultData.getInt("NEXTCURSORPOSITION");
            if (nextCursorPosition <= 0) {
                System.out.println("No more records to fetch. Pagination complete.");
                break; // No more records to fetch
            }

            // Update the cursor position for the next request
            cursorPosition = nextCursorPosition;
            batchNumber++;
        }

        return records;
    }

    /**
     * Sends a POST request with the given JSON body and returns the response.
     */
    private String sendPostRequest(String jsonBody) {
        try {
            URL apiUrl = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Tenant", TENANT_HEADER);
            connection.setRequestProperty("Organization", ORGANIZATION_HEADER);
            connection.setRequestProperty("X-Api-Key", API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

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
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    System.out.println("Error Response: " + errorResponse.toString());
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}