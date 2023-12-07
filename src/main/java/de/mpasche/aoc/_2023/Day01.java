package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Challenge(year = 2023, day = 1)
public class Day01 implements Day
{
  private final List<String> input;

  public Day01()
  {
    this.input = InputUtils.readInputFileByLine(2023, 1);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The sum of all calibration values is {}.", input.stream()
      .map(this::decodeCalibration)
      .mapToInt(Integer::parseInt)
      .sum());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The sum of all calibration values is {}.", input.stream()
      .map(this::replaceDigitWords)
      .map(this::decodeCalibration)
      .mapToInt(Integer::parseInt)
      .sum());
  }

  private String decodeCalibration(final String line)
  {
    final char[] chars = line.toCharArray();
    final StringBuilder sb = new StringBuilder();

    for(int i = 0; i < chars.length; i++)
    {
      final char character = chars[i];
      if(Character.isDigit(character))
      {
        sb.append(character);
        break;
      }
    }

    for(int i = chars.length - 1; i >= 0; i--)
    {
      final char character = chars[i];
      if(Character.isDigit(character))
      {
        sb.append(character);
        break;
      }
    }

    return sb.toString();
  }

  private String replaceDigitWords(final String line)
  {
    return line
      .replaceAll("one", "o1e")
      .replaceAll("two", "t2o")
      .replaceAll("three", "t3e")
      .replaceAll("four", "f4r")
      .replaceAll("five", "f5e")
      .replaceAll("six", "s6x")
      .replaceAll("seven", "s7n")
      .replaceAll("eight", "e8t")
      .replaceAll("nine", "n9e");
  }
}