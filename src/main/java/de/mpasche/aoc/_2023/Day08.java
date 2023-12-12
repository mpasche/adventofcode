package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Challenge(year = 2023, day = 8)
public class Day08 implements Day
{
  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("[LR]");
  private static final Pattern NODE_PATTERN = Pattern.compile("([A-Z]+)\\s=\\s\\(([A-Z]+),\\s([A-Z]+)\\)");

  private final List<Integer> instructions;
  private final Map<String, List<String>> nodes;

  public Day08()
  {
    final String input = InputUtils.readInputFile(2023, 8);
    final String[] split = input.split("\\R\\R");

    this.instructions = parseInstructions(split[0]);
    this.nodes = parseNodes(Arrays.stream(split[1].split("\\R")).toList());
  }

  @Override
  public void task1()
  {
    log.info("Task 1: {} steps are required to reach ZZZ.", countSteps("AAA", end -> end.equals("ZZZ")));
  }

  @Override
  public void task2()
  {
    final List<String> startNodes = nodes.keySet().stream().filter(node -> node.endsWith("A")).toList();
    final List<Integer> stepCounts = startNodes.parallelStream().map(node -> countSteps(node, end -> end.endsWith("Z"))).toList();

    log.info("Task 2: It takes {} steps until all nodes end with Z.", stepCounts.stream()
      .map(BigInteger::valueOf)
      .reduce(BigInteger.ONE, (x, y) -> (x.multiply(y)).divide(x.gcd(y))) // lcm
      .longValue());
  }

  private List<Integer> parseInstructions(final String input)
  {
    return INSTRUCTION_PATTERN
      .matcher(input)
      .results()
      .map(MatchResult::group)
      .map(instruction -> instruction.equals("L") ? 0 : 1)
      .toList();
  }

  private Map<String, List<String>> parseNodes(final List<String> input)
  {
    final Map<String, List<String>> nodes = new HashMap<>();

    for(final String line : input)
    {
      final Matcher matcher = NODE_PATTERN.matcher(line);
      if(matcher.matches())
      {
        final String key = matcher.group(1);
        final String left = matcher.group(2);
        final String right = matcher.group(3);

        nodes.put(key, List.of(left, right));
      }
    }

    return nodes;
  }

  private int countSteps(final String startNode, final Predicate<String> isEndNode)
  {
    int steps = 0;
    String currentNode = startNode;

    while(!isEndNode.test(currentNode))
    {
      for(final int instruction : instructions)
      {
        steps++;
        currentNode = nodes.get(currentNode).get(instruction);
        if(isEndNode.test(currentNode))
        {
          break;
        }
      }
    }

    return steps;
  }
}