package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Challenge(year = 2022, day = 5)
public class Day05 implements Day
{
  private static final Pattern CRATE_PATTERN = Pattern.compile("\\d+");
  private static final Pattern MOVE_PATTERN = Pattern.compile("\\D*(\\d+)\\D*(\\d+)\\D*(\\d+)");

  private final String input;

  public Day05()
  {
    this.input = Input.readInputFile(2022, 5);
  }

  @Override
  public void task1()
  {
    // inputs[0] = crates; inputs[1] = moves
    final String[] inputs = input.split("\\R\\R");
    final List<Stack<Character>> stacks = decodeStacks(Arrays.stream(inputs[0].split("\\R")).collect(Collectors.toList()));
    final List<Move> moves = decodeMoves(Arrays.stream(inputs[1].split("\\R")).toList());
    for(final Move move : moves)
    {
      final Stack<Character> from = stacks.get(move.from);
      final Stack<Character> to = stacks.get(move.to);
      for(int i = 0; i < move.count; i++)
      {
        to.push(from.pop());
      }
    }
    printStacks(stacks);
    log.info("Task 1: {} crates on top.", stacks.stream().map(Stack::peek).map(Object::toString).collect(Collectors.joining()));
  }

  @Override
  public void task2()
  {
    // inputs[0] = crates; inputs[1] = moves
    final String[] inputs = input.split("\\R\\R");
    final List<Stack<Character>> stacks = decodeStacks(Arrays.stream(inputs[0].split("\\R")).collect(Collectors.toList()));
    final List<Move> moves = decodeMoves(Arrays.stream(inputs[1].split("\\R")).toList());
    for(final Move move : moves)
    {
      final Stack<Character> from = stacks.get(move.from);
      final Stack<Character> to = stacks.get(move.to);
      final Stack<Character> cache = new Stack<>();
      for(int i = 0; i < move.count; i++)
      {
        cache.push(from.pop());
      }
      for(int i = 0; i < move.count; i++)
      {
        to.push(cache.pop());
      }
    }
    printStacks(stacks);
    log.info("Task 2: {} crates on top.", stacks.stream().map(Stack::peek).map(Object::toString).collect(Collectors.joining()));
  }

  private List<Stack<Character>> decodeStacks(final List<String> input)
  {
    // matcher for the last line ( 1   2   3   4   5   6   7   8   9 )
    final Matcher matcher = CRATE_PATTERN.matcher(input.remove(input.size() - 1));
    final List<Integer> crateIndexes = matcher.results().map(MatchResult::start).toList();
    // init stacks
    final List<Stack<Character>> stacks = IntStream.range(0, crateIndexes.size()).mapToObj(i -> new Stack<Character>()).toList();
    // fill stack bottom to top
    for(int i = input.size() - 1; i >= 0; i--)
    {
      final String line = input.get(i);
      for(int j = 0; j < crateIndexes.size(); j++)
      {
        final char c = line.charAt(crateIndexes.get(j));
        if(!Character.isSpaceChar(c))
        {
          stacks.get(j).push(c);
        }
      }
    }
    return stacks;
  }

  private List<Move> decodeMoves(final List<String> input)
  {
    return input.stream()
      .map(MOVE_PATTERN::matcher)
      .filter(Matcher::find)
      .map(matcher -> new Move(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)) - 1))
      .toList();
  }

  private void printStacks(final List<Stack<Character>> stacks)
  {
    for(int i = 0; i < stacks.size(); i++)
    {
      log.debug("{}: {}", i + 1, stacks.get(i).toString());
    }
  }

  private record Move(int count, int from, int to) {}

}
