// HttpUtil.java
package com.sovereigncraft.mcnpubreg;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;

public class HttpUtil {

    private static final boolean useApacheClient = isJavaVersionBelow();

    private static boolean isJavaVersionBelow() {
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.startsWith("1.")) {
            javaVersion = javaVersion.substring(2, 3);
        } else {
            int dot = javaVersion.indexOf(".");
            if (dot != -1) { javaVersion = javaVersion.substring(0, dot); }
        }
        return Integer.parseInt(javaVersion) < 11;
    }

    public static String sendGetRequest(String url) throws IOException {
        if (useApacheClient) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                org.apache.http.HttpResponse response = httpClient.execute(request);
                return EntityUtils.toString(response.getEntity());
            }
        } else {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            return sendRequestUsingJavaHttpClient(httpClient, request);
        }
    }

    public static String sendPostRequest(String url, String data) throws IOException {
        if (useApacheClient) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost request = new HttpPost(url);
                request.setEntity(new StringEntity(data));
                request.setHeader("Content-Type", "application/json");
                org.apache.http.HttpResponse response = httpClient.execute(request);
                return EntityUtils.toString(response.getEntity());
            }
        } else {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();
            return sendRequestUsingJavaHttpClient(httpClient, request);
        }
    }

    private static String sendRequestUsingJavaHttpClient(HttpClient httpClient, HttpRequest request) throws IOException {
        try {
            java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}
