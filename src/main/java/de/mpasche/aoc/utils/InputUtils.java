package de.mpasche.aoc.utils;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * A utility class for loading and reading files.
 *
 * @author mpasche
 */
@Slf4j
@UtilityClass
public class InputUtils
{
  /**
   * Writes the given string data to the given file.
   *
   * @param file The file to write to.
   * @param data The string data to write.
   * @throws IOException if an I/O error occurs while writing to the file.
   */
  private static void writeInputFile(final File file, final String data)
    throws IOException
  {
    Files.write(data.getBytes(Charset.defaultCharset()), file);
  }

  /**
   * Loads the input file for the given year and day.
   * If the file doesn't exist, it is created and the input data is fetched from the Advent of Code website.
   *
   * @param year The year of the Advent of Code event.
   * @param day The day of the Advent of Code event.
   * @return The input file for the given year and day.
   * @throws IOException if an I/O error occurs while reading or writing to the file.
   * @throws InterruptedException if the current thread is interrupted while waiting for the input data to be fetched from the website.
   */
  private static File loadInputFile(final int year, final int day)
    throws IOException, InterruptedException
  {
    final File file = new File("src/main/resources/inputs/" + year + "/day" + (day <= 9 ? "0" + day : day) + ".txt");
    Files.createParentDirs(file);
    if(file.createNewFile() || file.length() == 0)
    {
      try
      {
        writeInputFile(file, WebClient.sendRequest(year, day).body());
      }
      catch(ConnectException e)
      {
        file.deleteOnExit();
        log.error("Status Code {}", e.getMessage());
        System.exit(1);
      }
    }
    return file;
  }

  /**
   * Reads the input file for the given year and day and returns its contents as a string.
   *
   * @param year The year of the input file to read.
   * @param day The day of the input file to read.
   * @return The contents of the input file as a string.
   * @throws RuntimeException if an error occurs while reading the file.
   */
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

  /**
   * Reads the input file for the given year and day and returns its contents as a list of strings,
   * with each line in the file as a separate string in the list.
   *
   * @param year The year of the input file to read.
   * @param day The day of the input file to read.
   * @return The contents of the input file as a list of strings.
   * @throws RuntimeException if an error occurs while reading the file.
   */
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
}
