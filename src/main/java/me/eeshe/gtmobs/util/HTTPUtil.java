package me.eeshe.gtmobs.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPUtil {
  private static final String POST_URL = "http://116.202.133.194:30173/";

  /**
   * Executes an HTTP GET request to the passed url.
   *
   * @param endpoint Endpoint the request will be made to.
   * @return HTTP response in the form of a String.
   */
  public static String get(String endpoint) {
    try {
      URL url = new URL(POST_URL + endpoint);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
      String response = bufferedReader.readLine();
      if (response == null) {
        response = "";
      }
      bufferedReader.close();
      return response;
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * Executes an HTTP GET request to the passed url.
   *
   * @param endpoint    String URL the request will be made to.
   * @param jsonHeaders JSON Headers to be used in the request.
   * @return Response code of the request.
   */
  public static int get(String endpoint, String jsonHeaders) {
    try {
      URL url = new URL(POST_URL + endpoint);
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("GET");
      httpURLConnection.setDoOutput(true);
      if (!jsonHeaders.isEmpty()) {
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(jsonHeaders.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
      }
      return httpURLConnection.getResponseCode();
    } catch (Exception e) {
      e.printStackTrace();
      return 404;
    }
  }

  /**
   * Makes an HTTP POST request to the post URL to upload the passed data.
   *
   * @param jsonString Data that will be posted.
   */
  public static void post(String endpoint, String jsonString) {
    try {
      URL url = new URL(POST_URL + endpoint);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(5000);
      connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setRequestMethod("POST");

      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
      outputStream.close();

      BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
      bufferedInputStream.close();
      connection.disconnect();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
