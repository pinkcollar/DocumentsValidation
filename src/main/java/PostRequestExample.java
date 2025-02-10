import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequestExample {

    public static void main(String[] args) {
        // URL for the POST request
        String url = "https://ca1.eam.hxgnsmartcloud.com:443/axis/restservices/grids";

        // JSON body for the POST request
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
                + "\"VALUE\": \"9748\""
                + "},"
                + "\"MULTIADDON_FILTERS\": {"
                + "\"MADDON_FILTER\": ["
                + "{"
                + "\"ALIAS_NAME\": \"PPO_PPM\","
                + "\"OPERATOR\": \"=\","
                + "\"VALUE\": \"MINOR-D:L381\""
                + "}"
                + "]"
                + "},"
                + "\"REQUEST_TYPE\": \"LIST.HEAD_DATA.STORED\""
                + "}";

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