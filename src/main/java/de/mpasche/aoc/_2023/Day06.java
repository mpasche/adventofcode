package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
@Challenge(year = 2023, day = 6)
public class Day06 implements Day
{
  private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

  private final List<String> input;

  public Day06()
  {
    this.input = InputUtils.readInputFileByLine(2023, 6);
  }

  @Override
  public void task1()
  {
    final List<Long> times = parseNumbers(input.get(0));
    final List<Long> distances = parseNumbers(input.get(1));

    log.info("Task 1: The multiplied number of ways to beat the record is {}.", IntStream
      .range(0, Math.min(times.size(), distances.size()))
      .mapToLong(i -> calculateNewRecordCount(times.get(i), distances.get(i)))
      .reduce(1L, (a, b) -> a * b));
  }

  @Override
  public void task2()
  {
    final long time = parseNumber(input.get(0).replaceAll("\\s", ""));
    final long distance = parseNumber(input.get(1).replaceAll("\\s", ""));

    log.info("Task 2: The record can be beaten in {} ways.", calculateNewRecordCount(time, distance));
  }

  private List<Long> parseNumbers(final String numbers)
  {
    return NUMBER_PATTERN
      .matcher(numbers)
      .results()
      .map(MatchResult::group)
      .map(Long::parseLong)
      .toList();
  }

  private long parseNumber(final String number)
  {
    final Matcher matcher = NUMBER_PATTERN.matcher(number);
    if(matcher.find())
    {
      return Long.parseLong(matcher.group());
    }
    throw new RuntimeException();
  }

  private long calculateNewRecordCount(final long time, final long distance)
  {
    return LongStream.range(0, time)
      .filter(ms -> ms * (time - ms) > distance)
      .count();
  }
}