import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryFromCSVAndPostRequest {

    public static void main(String[] args) {
        // Path to the CSV file
        String csvFilePath = "src/main/resources/Minor_PPM_Objects_Meter_Due.csv"; // Replace with the actual path to your CSV file

        // Read the CSV file and extract ppo_object and ppm_code values
        List<String[]> ppoObjectsAndPpmCodes = readPpoObjectsAndPpmCodesFromCSV(csvFilePath);

        if (ppoObjectsAndPpmCodes.isEmpty()) {
            System.out.println("No ppo_object or ppm_code values found in the CSV file.");
            return;
        }

        // Send POST requests for each pair of ppo_object and ppm_code
        for (String[] ppoObjectAndPpmCode : ppoObjectsAndPpmCodes) {
            String ppoObject = ppoObjectAndPpmCode[0];
            String ppmCode = ppoObjectAndPpmCode[1];

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

            // Send the POST request
            sendPostRequest(jsonBody);
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
     * Sends a POST request with the given JSON body.
     */
    private static void sendPostRequest(String jsonBody) {
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
            System.out.println("Response Code: " + responseCode);

            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    // Print the JSON response
                    System.out.println("Response: " + response.toString());
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
    }
}