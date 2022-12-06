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

  private static File loadInputFile(final int year, final int day)
    throws IOException, InterruptedException
  {
    final File file = getInputFile(year, day);
    Files.createParentDirs(file);
    if(file.createNewFile() || file.length() == 0)
    {
      try
      {
        writeInputFile(file, getRequest(year, day));
      }
      catch(IllegalArgumentException e)
      {
        file.deleteOnExit();
        log.error("Invalid cookie. Please update the file /resources/COOKIE with your session cookie.");
        System.exit(1);
      }
    }
    return file;
  }

  public static String readInputFile(final int year, final int day)
  {
    try
    {
      final File file = loadInputFile(year, day);
      return Resources.toString(file.toURI().toURL(), Charset.defaultCharset());
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static List<String> readInputFileByLine(final int year, final int day)
  {
    try
    {
      final File file = loadInputFile(year, day);
      return Files.readLines(file, Charset.defaultCharset());
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private Input() {}

}
