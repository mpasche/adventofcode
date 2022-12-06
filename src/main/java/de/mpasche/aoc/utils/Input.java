package de.mpasche.aoc.utils;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
public class Input
{
  private static File getInputFile(final int year, final int day)
  {
    return new File("src/main/resources/inputs/" + year + "/day" + day + ".txt");
  }

  private static String getRequest(final int year, final int day)
    throws IOException, InterruptedException, IllegalArgumentException
  {
    final String url = MessageFormat.format(Config.getInputURL(), Integer.toString(year), Integer.toString(day));
    final HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .GET()
      .build();

    final HttpResponse<String> response = WebClient.getClient().send(request, HttpResponse.BodyHandlers.ofString());
    if(response.statusCode() != HttpURLConnection.HTTP_OK)
    {
      // cannot log in -> invalid cookie
      throw new IllegalArgumentException();
    }
    return response.body();
  }

  private static void writeInputFile(final File file, final String input)
    throws IOException
  {
    try(final BufferedWriter writer = Files.newWriter(file, Charset.defaultCharset()))
    {
      writer.write(input);
    }
  }

  public static void loadData(final int year, final int day)
  {
    final File file = getInputFile(year, day);
    try
    {
      Files.createParentDirs(file);
      if(file.createNewFile() || file.length() == 0)
      {
        writeInputFile(file, getRequest(year, day));
      }
    }
    catch(IllegalArgumentException e)
    {
      file.deleteOnExit();
      log.error("Invalid cookie. Please update the file /resources/COOKIE with your session cookie.");
      System.exit(1);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static String readInputFile(final int year, final int day)
  {
    try
    {
      return Resources.toString(getInputFile(year, day).toURI().toURL(), Charset.defaultCharset());
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static List<String> readInputFileByLine(final int year, final int day)
  {
    try
    {
      return Files.readLines(getInputFile(year, day), Charset.defaultCharset());
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  private Input() {}

}
