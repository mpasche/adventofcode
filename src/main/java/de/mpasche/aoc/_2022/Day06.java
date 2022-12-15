package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Challenge(year = 2022, day = 6)
public class Day06 implements Day
{
  private final String input;

  public Day06()
  {
    this.input = InputUtils.readInputFile(2022, 6);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: Marker was found at {}.", findMarker(4));
  }

  @Override
  public void task2()
  {
    log.info("Task 2: Marker was found at {}.", findMarker(14));
  }

  private int findMarker(final int size)
  {
    for(int i = 0; i < input.length(); i++)
    {
      if(input.substring(i, i + size).chars().distinct().count() == size)
      {
        return size + i;
      }
    }
    return -1;
  }
}
