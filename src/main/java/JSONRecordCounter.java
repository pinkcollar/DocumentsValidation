import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONRecordCounter {

    // URL and headers for the POST request
    private static final String API_URL = "https://ca1.eam.hxgnsmartcloud.com:443/axis/restservices/grids";
    private static final String TENANT_HEADER = "BVBWV1707339191_DEV";
    private static final String ORGANIZATION_HEADER = "*";
    private static final String API_KEY = "se13180466-50d9-4272-9fdd-e209a822f905";

    public static void main(String[] args) {
        JSONRecordCounter counter = new JSONRecordCounter();

        // Example usage: Specify GRID_NAME, ALIAS_NAME, and VALUE
        String gridName = "1UTREA"; // Replace with the desired grid name
        String aliasName = "rea_lastsaved"; // Replace with the desired alias name
        String filterValue = "02/06/2025 15:50"; // Replace with the desired filter value
        int totalRecords = counter.countJSONRecords(gridName, aliasName, filterValue);

        System.out.println("Total records retrieved: " + totalRecords);
    }

    /**
     * Counts the number of records retrieved from the JSON response.
     *
     * @param gridName   The name of the grid to query (e.g., "1UTREA").
     * @param aliasName  The alias name for filtering (e.g., "rea_lastsaved").
     * @param filterValue The value to use in the filter (e.g., "02/01/2025 15:50").
     * @return The total number of records retrieved.
     */
    public int countJSONRecords(String gridName, String aliasName, String filterValue) {
        int totalRecords = 0;
        int cursorPosition = 0;
        int batchNumber = 1;

        while (true) {
            // Construct the JSON body with the current cursor position
            String jsonBody = "{"
                    + "\"GRID_TYPE\": {"
                    + "\"TYPE\": \"LIST\""
                    + "},"
                    + "\"GRID\": {"
                    + "\"CURRENT_TAB_NAME\": \"LST\","
                    + "\"GRID_NAME\": \"" + gridName + "\","
                    + "\"NUMBER_OF_ROWS_FIRST_RETURNED\": 10000,"
                    + "\"CURSOR_POSITION\": " + cursorPosition
                    + "},"
                    + "\"MULTIADDON_FILTERS\": {"
                    + "\"MADDON_FILTER\": ["
                    + "{"
                    + "\"ALIAS_NAME\": \"" + aliasName + "\","
                    + "\"OPERATOR\": \">=\","
                    + "\"VALUE\": \"" + filterValue + "\""
                    + "}"
                    + "]"
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
            int recordsInBatch = dataRecords.length();
            totalRecords += recordsInBatch;

            System.out.println("Retrieved " + recordsInBatch + " records in batch #" + batchNumber);
            System.out.println("Cumulative total records: " + totalRecords);

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

        return totalRecords;
    }

    /**
     * Sends a POST request with the given JSON body and returns the response.
     */
    private String sendPostRequest(String jsonBody) {
        try {
            // Create the URL object
            URL apiUrl = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set the request headers
            connection.setRequestProperty("Tenant", TENANT_HEADER);
            connection.setRequestProperty("Organization", ORGANIZATION_HEADER);
            connection.setRequestProperty("X-Api-Key", API_KEY);
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
}