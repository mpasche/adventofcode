package de.mpasche.aoc._2022;

import com.google.common.collect.Range;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Date;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Date(year = 2022, day = 4)
public class Day4 implements Challenge
{
  private final List<String> input;

  public Day4()
  {
    input = Input.readInputFileByLine(2022, 4);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: {} assignments fully contain the other.", input.stream().map(this::mapToRanges).filter(this::isFullyContained).count());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: {} assignments overlap.", input.stream().map(this::mapToRanges).filter(this::isOverlapping).count());
  }

  private Map.Entry<Range<Integer>, Range<Integer>> mapToRanges(final String input)
  {
    final String[] split = input.split(",");
    return Map.entry(getRange(split[0]), getRange(split[1]));
  }

  private Range<Integer> getRange(final String input)
  {
    final String[] split = input.split("-");
    final int lowerAssignment = Integer.parseInt(split[0]);
    final int upperAssignment = Integer.parseInt(split[1]);
    return Range.closed(lowerAssignment, upperAssignment);
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
