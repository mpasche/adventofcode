package de.mpasche.aoc._2022;

import com.google.common.collect.Lists;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Challenge(year = 2022, day = 3)
public class Day03 implements Day
{
  private final List<String> input;

  public Day03()
  {
    input = InputUtils.readInputFileByLine(2022, 3);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: Priority {}", input.stream().map(this::getCompartments).mapToInt(this::findIntersection).map(this::getPriority).sum());
  }

  @Override
  public void task2()
  {
    final List<List<String>> groups = Lists.partition(input, 3);
    log.info("Task 2: Priority {}", groups.stream().mapToInt(this::findIntersection).map(this::getPriority).sum());
  }

  private List<String> getCompartments(final String input)
  {
    final int index = Math.floorDiv(input.length(), 2);
    return List.of(input.substring(0, index), input.substring(index));
  }

  private int findIntersection(final List<String> input)
  {
    if(input.size() < 2)
    {
      throw new RuntimeException();
    }
    final Set<Integer> intersections = new HashSet<>(input.get(0).chars().boxed().toList());
    for(int i = 1; i < input.size(); i++)
    {
      intersections.retainAll(input.get(i).chars().boxed().toList());
    }
    if(intersections.size() != 1)
    {
      throw new RuntimeException();
    }
    return intersections.iterator().next();
  }

  private int getPriority(final int input)
  {
    return Character.isUpperCase(input)
      ? input - (int) 'A' + 27
      : input - (int) 'a' + 1;
  }
}
