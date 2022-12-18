package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Challenge(year = 2022, day = 11)
public class Day11 implements Day
{
  private final String input;

  public Day11()
  {
    this.input = InputUtils.readInputFile(2022, 11);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: Monkey business is {}.", Arrays.stream(simulateMonkeys(20, true))
      .boxed()
      .sorted(Comparator.reverseOrder())
      .limit(2)
      .reduce(1L, (x, y) -> x * y));
  }

  @Override
  public void task2()
  {
    log.info("Task 2: Monkey business is {}.", Arrays.stream(simulateMonkeys(10000, false))
      .boxed()
      .sorted(Comparator.reverseOrder())
      .limit(2)
      .reduce(1L, (x, y) -> x * y));
  }

  private long[] simulateMonkeys(final int rounds, final boolean relief)
  {
    final List<Monkey> monkeys = decodeMonkeys(input);
    final long lcm = monkeys.stream()
      .map(Monkey::divider)
      .map(BigInteger::valueOf)
      .reduce(BigInteger.ONE, (x, y) -> (x.multiply(y)).divide(x.gcd(y)))
      .longValue();

    final long[] inspected = new long[monkeys.size()];
    for(int i = 0; i < rounds; i++)
    {
      for(int j = 0; j < monkeys.size(); j++)
      {
        final Monkey monkey = monkeys.get(j);
        for(Long item : monkey.items)
        {
          inspected[j]++;
          item = monkey.inspectItem(item);
          if(relief)
          {
            item = Math.floorDiv(item, 3);
          }
          item = Math.floorMod(item, lcm);
          monkeys.get(monkey.throwItem(item)).items.add(item);
        }
        monkey.items.clear();
      }
    }
    return inspected;
  }

  private List<Monkey> decodeMonkeys(final String input)
  {
    return Arrays.stream(input.split("\\R\\R")).map(this::decodeMonkey).toList();
  }

  private Monkey decodeMonkey(final String input)
  {
    final String[] lines = input.split("\\R");

    // Parse the first line and extract the items
    final String[] itemString = lines[1].split(": ")[1].split(", ");
    final List<Long> items = Arrays.stream(itemString).map(Long::parseLong).collect(Collectors.toList());

    // Parse the second line and extract the operator and operand
    final String[] operationString = lines[2].split("old ")[1].split(" ");
    final String operator = operationString[0];
    final String operand = operationString[1];

    // Parse the remaining lines
    final int divider = Integer.parseInt(lines[3].split("by ")[1]);
    final int ifTrue = Integer.parseInt(lines[4].split("monkey ")[1]);
    final int ifFalse = Integer.parseInt(lines[5].split("monkey ")[1]);

    return new Monkey(items, operator, operand, divider, ifTrue, ifFalse);
  }

  private record Monkey(List<Long> items, String operator, String operand, int divider, int ifTrue, int ifFalse)
  {
    public long inspectItem(final long item)
    {
      final long op = operand.equals("old") ? item : Long.parseLong(operand);
      return switch(operator)
      {
        case "+" -> item + op;
        case "*" -> item * op;
        default -> throw new RuntimeException();
      };
    }

    public int throwItem(final long item)
    {
      return item % divider == 0 ? ifTrue : ifFalse;
    }
  }
}
