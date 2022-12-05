package de.mpasche.aoc.utils;

import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;

public class Input
{
  private static File getInputFile(final int year, final int day)
  {
    return new File("src/main/resources/inputs/" + year + "/day" + day + ".txt");
  }

  private static String sendRequest(final int year, final int day)
  {
    final String url = MessageFormat.format(Config.getInputURL(), Integer.toString(year), Integer.toString(day));
    final HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .GET()
      .build();

    try
    {
      return Client.getClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private static void writeInputFile(final File file, final String data)
  {
    try(final BufferedWriter writer = Files.newWriter(file, Charset.defaultCharset()))
    {
      writer.write(data);
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
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
        writeInputFile(file, sendRequest(year, day));
      }
    }
    catch(IOException e)
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
