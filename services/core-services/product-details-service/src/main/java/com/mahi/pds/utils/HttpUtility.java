package com.mahi.pds.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
 
public class HttpUtility {
 
    public static String sendGetRequest(String urlString, Map<String, String> queryParams, Map<String, String> headers, Map<String, String> cookies, Proxy proxy) throws IOException {
 
        StringBuilder fullUrl = new StringBuilder(urlString);
 
        // Append query parameters
        if (queryParams != null && !queryParams.isEmpty()) {
            fullUrl.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                fullUrl.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            fullUrl.deleteCharAt(fullUrl.length() - 1); // Remove the trailing '&'
            System.out.println(fullUrl);
        }
 
        URL url = new URL(fullUrl.toString());
 
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
 
        // Set request headers
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
 
        // Set cookies
//        if (cookies != null && !cookies.isEmpty()) {
//            StringBuilder cookieStringBuilder = new StringBuilder();
//            for (Map.Entry<String, String> entry : cookies.entrySet()) {
//                if (cookieStringBuilder.length() > 0) {
//                    cookieStringBuilder.append("; ");
//                }
//                cookieStringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
//            }
//            connection.setRequestProperty("Cookie", cookieStringBuilder.toString());
//        }
 
        int responseCode = connection.getResponseCode();
 
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
 
            return response.toString();
 
        } else {
            throw new IOException("HTTP GET request failed with response code: " + responseCode);
        }
    }
 
    public static void main(String[] args) {
 
        try {
//            String urlString = "https://purpleid-test.oktapreview.com/oauth2/aus1hwg3myjLb30AQ0h8/v1/token";
//            Map<String, String> parameters = new HashMap<>();
//            parameters.put("grant_type", "client_credentials");
//            parameters.put("response_type", "token");
//            parameters.put("scope", "Custom_Scope");
//            parameters.put("client_secret", "3TttYHBZanyhaFdPRcYo_4f4IDQp_yffduxpUIQx");
//            parameters.put("client_id", "0oa1jlzi6dzF5MomU0h8");
           
            String urlString = "https://dev-59927831.okta.com/oauth2/default"+"/v1/token";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("grant_type", "client_credentials");
            parameters.put("scope", "mahendra");
            parameters.put("client_secret", "D5fN6kGCNvrR7i5lJdjEbleSS_9BP0yipNnX8Jvz");
            parameters.put("client_id", "0oa9jlr1h0HmaqI0P5d7");
           
               
 
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
 
            Map<String, String> cookies = new HashMap<>();
//            cookies.put("JSESSIONID", "09D05A0305A1AFE17A527E0FFD92F3F4");
//            cookies.put("t", "default");
//            cookies.put("DT", "DI1BAn6QyBDQTa7PfMS47NdJg");
 
//             Proxy settings
            String proxyHost = "internet.proxy.fedex.com";
            int proxyPort = 3150;
 
            // Create proxy object
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

            //as of now proxy is not needed
            String response = sendGetRequest(urlString, parameters, headers, cookies, proxy);
            System.out.println("Response from server: " + response);
        } catch (IOException e) {
            System.err.println("Error sending POST request: " + e.getMessage());
        }
 
    }
 
}
