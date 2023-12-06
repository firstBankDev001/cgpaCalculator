package main.java;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class HttpsCallExample {
    public static void main(String[] args) {
        String endpointUrl = "https://kcb-lami-dmvic-aki-integration-kcb-lami-apis.apps.test.aro.kcbgroup.com/api/V4/IntermediaryIntegration/IssuanceTypeCCertificate";
        String username = "admin";
        String password = "admin";

        try {
        	// Disable SSL verification
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            
            // Disabling Ends Here

            URL url = new URL(endpointUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Set up the connection properties for a POST request with JSON data.
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set Basic Authorization header
            String credentials = username + ":" + password;
            String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", authHeaderValue);

            // Set Content-Type header for JSON data
            connection.setRequestProperty("Content-Type", "application/json");

            // JSON data to send in the request body
            String jsonData = "{\r\n\"Certificate\": \"C\",\r\n\"Membercompanyid\": \"21\",\r\n\"Typeofcover\": \"100\",\r\n\"Policyholder\": \"JOHN CLINTON\",\r\n\"policynumber\": \"P-2018-100-9002-95-9689\",\r\n\"Commencingdate\": \"26/07/2023\",\r\n\"Expiringdate\": \"25/07/2024\",\r\n\"Registrationnumber\": \"KTU 450J\",\r\n\"Chassisnumber\": \"CHA1232345\",\r\n\"Phonenumber\": \"724384488\",\r\n\"Bodytype\": \"BT\",\r\n\"Vehiclemake\": \"ABARTH\",\r\n\"Vehiclemodel\": \"131 RALLY\",\r\n\"Email\": \"david.ogubi@turnkeyafrica.com\",\r\n\"SumInsured\": \"1200000.00\",\r\n\"InsuredPIN\": \"A079999999X\",\r\n\"Yearofmanufacture\": \"2011\"\r\n}";

            // Write the JSON data to the request body
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // Make the request and get the response.
            int responseCode = connection.getResponseCode();

            // Read the response data.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Handle the response data as needed.
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Request successful!");
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("Request failed! Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}