package de.mpasche.aoc._2022;

import com.google.common.collect.Lists;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Date;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Date(year = 2022, day = 3)
public class Day3 implements Challenge
{
  private final List<String> input;

  public Day3()
  {
    input = Input.readInputFileByLine(2022, 3);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: Priority {}", input.stream().map(this::mapToCompartments).mapToInt(this::findCommonItem).map(this::getPriority).sum());
  }

  @Override
  public void task2()
  {
    final List<List<String>> groups = Lists.partition(input, 3);
    log.info("Task 2: Priority {}", groups.stream().mapToInt(this::findCommonItem).map(this::getPriority).sum());
  }

  private List<String> mapToCompartments(final String input)
  {
    final int index = Math.floorDiv(input.length(), 2);
    return List.of(input.substring(0, index), input.substring(index));
  }

  private int findCommonItem(final List<String> input)
  {
    if(input.size() < 2)
    {
      throw new RuntimeException();
    }
    final HashSet<Integer> result = new HashSet<>(input.get(0).chars().boxed().toList());
    for(int i = 1; i < input.size(); i++)
    {
      result.retainAll(input.get(i).chars().boxed().toList());
    }
    if(result.size() != 1)
    {
      throw new RuntimeException();
    }
    return result.iterator().next();
  }

  private int getPriority(final int input)
  {
    return Character.isUpperCase(input)
      ? input - (int) 'A' + 27
      : input - (int) 'a' + 1;
  }
}
