package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;

@Slf4j
@Challenge(year = 2022, day = 1)
public class Day01 implements Day
{
  private final String input;

  public Day01()
  {
    this.input = InputUtils.readInputFile(2022, 1);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: {} Calories", Arrays.stream(input.split("\\R\\R"))
      .mapToInt(part -> Arrays.stream(part.split("\\R")).mapToInt(Integer::parseInt).sum())
      .max()
      .orElseThrow());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: {} Calories", Arrays.stream(input.split("\\R\\R"))
      .map(part -> Arrays.stream(part.split("\\R")).mapToInt(Integer::parseInt).sum())
      .sorted(Comparator.reverseOrder())
      .mapToInt(i -> i)
      .limit(3)
      .sum());
  }
}
