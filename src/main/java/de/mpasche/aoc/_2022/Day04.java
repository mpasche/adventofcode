package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Challenge(year = 2022, day = 4)
public class Day04 implements Day
{
  private final List<String> input;

  public Day04()
  {
    input = InputUtils.readInputFileByLine(2022, 4);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: {} assignments fully contain the other.", input.stream()
      .map(this::decodeSections)
      .filter(s -> (s[0] <= s[2] && s[1] >= s[3]) || (s[0] >= s[2] && s[1] <= s[3]))
      .count());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: {} assignments overlap.", input.stream()
      .map(this::decodeSections)
      .filter(s -> s[0] <= s[3] && s[1] >= s[2])
      .count());
  }

  private int[] decodeSections(final String line)
  {
    return Arrays.stream(line.split("[-,]")).mapToInt(Integer::parseInt).toArray();
  }
}
