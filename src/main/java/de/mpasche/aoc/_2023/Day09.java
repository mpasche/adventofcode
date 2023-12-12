package de.mpasche.aoc._2023;

import com.google.common.collect.Lists;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Challenge(year = 2023, day = 9)
public class Day09 implements Day
{
  private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+");

  private final List<List<Long>> numbers;

  public Day09()
  {
    final List<String> input = InputUtils.readInputFileByLine(2023, 9);

    this.numbers = parseInput(input);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The sum of the extrapolated values is {}.", numbers.parallelStream()
      .mapToLong(this::calculatePrediction)
      .sum());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The sum of the extrapolated values is {}.", numbers.parallelStream()
      .map(Lists::reverse)
      .mapToLong(this::calculatePrediction)
      .sum());
  }

  private List<List<Long>> parseInput(final List<String> input)
  {
    return input.stream().map(this::parseNumbers).toList();
  }

  private List<Long> parseNumbers(final String numbers)
  {
    return NUMBER_PATTERN
      .matcher(numbers)
      .results()
      .map(MatchResult::group)
      .map(Long::parseLong)
      .collect(Collectors.toList());
  }

  private long calculatePrediction(final List<Long> numbers)
  {
    final List<List<Long>> differences = calculateDifferences(numbers);

    for(int i = differences.size() - 2; i >= 0; i--)
    {
      final List<Long> currentDifferences = differences.get(i);
      final List<Long> previousDifferences = differences.get(i + 1);

      final long currentValue = currentDifferences.get(currentDifferences.size() - 1);
      final long previousValue = previousDifferences.get(previousDifferences.size() - 1);

      currentDifferences.add(currentValue + previousValue);
    }

    final List<Long> numbersWithPrediction = differences.get(0);
    return numbersWithPrediction.get(numbersWithPrediction.size() - 1);
  }

  private List<List<Long>> calculateDifferences(final List<Long> numbers)
  {
    final List<List<Long>> differences = new ArrayList<>();
    differences.add(numbers);

    List<Long> currentNumbers = numbers;
    while(currentNumbers.size() > 1 && !currentNumbers.stream().allMatch(number -> number == 0))
    {
      final List<Long> nextDifferences = new ArrayList<>();
      for(int i = 0; i < currentNumbers.size() - 1; i++)
      {
        nextDifferences.add(currentNumbers.get(i + 1) - currentNumbers.get(i));
      }
      currentNumbers = nextDifferences;
      differences.add(nextDifferences);
    }

    return differences;
  }
}