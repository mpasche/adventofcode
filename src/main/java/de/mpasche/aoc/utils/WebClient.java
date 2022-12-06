package de.mpasche.aoc.utils;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

public class WebClient
{
  public static HttpClient getClient()
  {
    final HttpCookie sessionCookie = new HttpCookie("session", Config.getSession().trim());
    sessionCookie.setPath("/");
    sessionCookie.setVersion(0);

    final CookieManager manager = new CookieManager();
    manager.getCookieStore().add(URI.create(Config.getURL()), sessionCookie);

    return HttpClient.newBuilder()
      .cookieHandler(manager)
      .connectTimeout(Duration.ofSeconds(5))
      .build();
  }

  private WebClient() {}

}
