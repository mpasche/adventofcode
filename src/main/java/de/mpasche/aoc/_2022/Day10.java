package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Challenge(year = 2022, day = 10)
public class Day10 implements Day
{
  private final Pattern INSTRUCTION_PATTERN = Pattern.compile("(\\w+)\\s*(-?\\d+)?");

  private final List<Integer> register;

  public Day10()
  {
    final List<String> input = InputUtils.readInputFileByLine(2022, 10);
    final List<Instruction> instructions = decodeInstructions(input);
    this.register = simulateRegister(instructions);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The signal strength is {}.", IntStream.of(20, 60, 100, 140, 180, 220).map(cycle -> cycle * register.get(cycle - 1)).sum());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: CRT:");
    for(int i = 0; i < 6; i++)
    {
      final int start = i * 40;
      final int end = start + 40;
      log.info(IntStream.range(start, end).mapToObj(this::getPixelAt).collect(Collectors.joining()));
    }
  }

  private List<Instruction> decodeInstructions(final List<String> input)
  {
    return input.stream()
      .map(INSTRUCTION_PATTERN::matcher)
      .filter(Matcher::matches)
      .map(matcher -> new Instruction(matcher.group(1), matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2))))
      .toList();
  }

  private List<Integer> simulateRegister(final List<Instruction> instructions)
  {
    final List<Integer> values = new ArrayList<>();
    values.add(1);
    for(final Instruction instruction : instructions)
    {
      final int value = values.get(values.size() - 1);
      values.add(value);
      if(instruction.instruction.equals("addx"))
      {
        values.add(value + instruction.value);
      }
    }
    return values;
  }

  private String getPixelAt(final int cycle)
  {
    final int value = register.get(cycle);
    return (cycle % 40 == value - 1 || cycle % 40 == value || cycle % 40 == value + 1) ? "#" : " ";
  }

  private record Instruction(String instruction, int value) {}

}
