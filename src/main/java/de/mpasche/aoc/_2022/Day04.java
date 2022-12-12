package de.mpasche.aoc._2022;

import com.google.common.collect.Range;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Challenge(year = 2022, day = 4)
public class Day04 implements Day
{
  private final List<String> input;

  public Day04()
  {
    input = Input.readInputFileByLine(2022, 4);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: {} assignments fully contain the other.", input.stream().map(this::getRanges).filter(this::isFullyContained).count());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: {} assignments overlap.", input.stream().map(this::getRanges).filter(this::isOverlapping).count());
  }

  private Map.Entry<Range<Integer>, Range<Integer>> getRanges(final String input)
  {
    final String[] split = input.split("[-,]");
    return Map.entry(getRange(split[0], split[1]), getRange(split[2], split[3]));
  }

  private Range<Integer> getRange(final String lower, final String upper)
  {
    return Range.closed(Integer.parseInt(lower), Integer.parseInt(upper));
  }

  private boolean isFullyContained(final Map.Entry<Range<Integer>, Range<Integer>> pair)
  {
    final Range<Integer> firstRange = pair.getKey();
    final Range<Integer> secondRange = pair.getValue();
    return firstRange.encloses(secondRange) || secondRange.encloses(firstRange);
  }

  private boolean isOverlapping(final Map.Entry<Range<Integer>, Range<Integer>> pair)
  {
    final Range<Integer> firstRange = pair.getKey();
    final Range<Integer> secondRange = pair.getValue();
    return firstRange.isConnected(secondRange);
  }
}
