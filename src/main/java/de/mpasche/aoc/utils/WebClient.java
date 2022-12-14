package de.mpasche.aoc.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Duration;

/**
 * A utility class for sending HTTP requests and receiving responses from a server.
 *
 * @author mpasche
 */
@Slf4j
@UtilityClass
public class WebClient
{
  /**
   * Sends an HTTP request to a URL constructed using the specified year and day.
   *
   * @param year The year to use in the URL.
   * @param day The day to use in the URL.
   * @return The response from the server.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the thread is interrupted.
   */
  public static HttpResponse<String> sendRequest(final int year, final int day)
    throws IOException, InterruptedException
  {
    final String url = MessageFormat.format(Config.getInputURL(), Integer.toString(year), Integer.toString(day));
    return sendRequest(createRequest(url));
  }

  /**
   * Creates an HTTP GET request for the specified URL.
   *
   * @param url The URL for the request.
   * @return The request object.
   */
  private static HttpRequest createRequest(final String url)
  {
    return HttpRequest.newBuilder()
      .uri(URI.create(url))
      .GET()
      .build();
  }

  /**
   * Sends the specified HTTP request and returns the response from the server.
   * The response is converted to a string and passed to the handleResponse method for further processing.
   *
   * @param request The HTTP request to send.
   * @return The response from the server.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the thread is interrupted.
   * @see #handleResponse(HttpResponse)
   */
  private static HttpResponse<String> sendRequest(final HttpRequest request)
    throws IOException, InterruptedException
  {
    final HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
    handleResponse(response);
    return response;
  }

  /**
   * Creates and returns an HttpClient object that is configured with a cookie handler and a connection timeout of 5 seconds.
   * The cookie handler is obtained by calling the getCookieHandler method.
   *
   * @return The HttpClient object.
   * @see #getCookieHandler()
   */
  private static HttpClient getClient()
  {
    return HttpClient.newBuilder()
      .cookieHandler(getCookieHandler())
      .connectTimeout(Duration.ofSeconds(5))
      .build();
  }

  /**
   * Creates and returns a CookieManager object that contains a cookie obtained by calling the getCookie method.
   *
   * @return The CookieManager object.
   * @see #getCookie()
   */
  private static CookieHandler getCookieHandler()
  {
    final CookieManager manager = new CookieManager();
    manager.getCookieStore().add(URI.create(Config.getURL()), getCookie());
    return manager;
  }

  /**
   * Creates and returns an HttpCookie object that represents a session cookie with the name "session" and the value obtained from the Config class.
   *
   * @return The HttpCookie object.
   * @see Config#getSession()
   */
  private static HttpCookie getCookie()
  {
    final HttpCookie sessionCookie = new HttpCookie("session", Config.getSession());
    sessionCookie.setPath("/");
    sessionCookie.setVersion(0);
    return sessionCookie;
  }

  /**
   * Handles the response from an HTTP request.
   *
   * @param response The response to handle.
   * @throws ConnectException If the response has an error status code.
   */
  private static void handleResponse(final HttpResponse<?> response)
    throws ConnectException
  {
    switch(response.statusCode())
    {
      case HttpURLConnection.HTTP_OK -> { return; }
      case HttpURLConnection.HTTP_BAD_REQUEST -> log.error("Invalid cookie. Please update the file '/resources/COOKIE' with your session cookie.");
      case HttpURLConnection.HTTP_NOT_FOUND -> log.error("Invalid challenge.");
    }
    throw new ConnectException(String.valueOf(response.statusCode()));
  }
}
