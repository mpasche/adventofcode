package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Slf4j
@Challenge(year = 2023, day = 15)
public class Day15 implements Day
{
  private static final Pattern STEP_PATTERN = Pattern.compile("([a-zA-Z]+)[-=]([0-9]*)");

  final String input;

  public Day15()
  {
    this.input = InputUtils.readInputFile(2023, 15);
  }

  @Override
  public void task1()
  {
    final String[] steps = input.replaceAll("\\R", "").split(",");
    log.info("Task 1: The sum of the result is {}.", Arrays.stream(steps)
      .mapToInt(this::calculateHash)
      .sum());
  }

  @Override
  public void task2()
  {
    final Map<Integer, LinkedList<Lens>> boxes = initializeBoxes(input);
    final int focusingPower = calculateFocusingPower(boxes);
    log.info("Task 2: The focusing power of the lens configuration is {}.", focusingPower);
  }

  private int calculateHash(final String input)
  {
    int hash = 0;
    for(final char current : input.toCharArray())
    {
      hash += current;
      hash *= 17;
      hash %= 256;
    }
    return hash;
  }

  private Map<Integer, LinkedList<Lens>> initializeBoxes(final String input)
  {
    final Map<Integer, LinkedList<Lens>> boxes = new HashMap<>();

    final List<MatchResult> steps = STEP_PATTERN.matcher(input).results().toList();
    for(final MatchResult step : steps)
    {
      final String label = step.group(1);
      final String focalLength = step.group(2);
      final int box = calculateHash(label);

      final LinkedList<Lens> lenses = boxes.compute(box, (k, v) -> v == null ? new LinkedList<>() : v);
      final Lens existingLens = lenses.stream().filter(l -> l.label.equals(label)).findFirst().orElse(null);
      final int index = lenses.indexOf(existingLens);

      if(focalLength != null && !focalLength.isEmpty())
      {
        final Lens lens = new Lens(label, Integer.parseInt(focalLength));
        if(index >= 0)
        {
          lenses.set(index, lens);
        }
        else
        {
          lenses.add(lens);
        }
      }
      else if(index >= 0)
      {
        lenses.remove(index);
      }
    }

    return boxes;
  }

  private int calculateFocusingPower(final Map<Integer, LinkedList<Lens>> boxes)
  {
    int focusingPower = 0;
    for(final var entry : boxes.entrySet())
    {
      final int box = entry.getKey() + 1;
      final LinkedList<Lens> lenses = entry.getValue();
      for(int i = 0; i < lenses.size(); i++)
      {
        focusingPower += box * (i + 1) * lenses.get(i).focalLength;
      }
    }
    return focusingPower;
  }

  private record Lens(String label, int focalLength) {}

}