package de.mpasche.aoc.utils;

import com.google.common.io.Resources;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

@Slf4j
@UtilityClass
public class Config
{
  private static Properties PROPERTIES;

  private static Properties getProperties()
  {
    if(PROPERTIES == null)
    {
      PROPERTIES = new Properties();
      try
      {
        final URL resource = Resources.getResource("application.properties");
        PROPERTIES.load(resource.openStream());
      }
      catch(Exception e)
      {
        log.error("application.properties not found.", e);
        System.exit(1);
      }
    }
    return PROPERTIES;
  }

  public static String getURL()
  {
    return getProperties().getProperty("data.url").trim();
  }

  public static String getInputURL()
  {
    return getProperties().getProperty("data.url.input").trim();
  }

  public static String getSession()
  {
    try
    {
      final URL cookie = Resources.getResource("COOKIE");
      return Resources.toString(cookie, Charset.defaultCharset()).trim();
    }
    catch(Exception e)
    {
      log.error("Please create the file /resources/COOKIE with your session cookie.");
      System.exit(1);
      return null;
    }
  }
}
