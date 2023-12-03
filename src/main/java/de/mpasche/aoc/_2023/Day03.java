package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import de.mpasche.aoc.utils.PointUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Challenge(year = 2023, day = 3)
public class Day03 implements Day
{
  private final Map<Symbol, List<Part>> symbolToAdjacentParts;

  public Day03()
  {
    final List<String> input = InputUtils.readInputFileByLine(2023, 3);

    this.symbolToAdjacentParts = createSymbolToAdjacentPartsMap(parseSymbols(input), parseParts(input));
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The sum of all part numbers in the engine schematic is {}.", symbolToAdjacentParts.values()
      .stream()
      .flatMap(Collection::stream)
      .distinct()
      .mapToInt(Part::number)
      .sum());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The sum of all gear ratios in the engine schematic is {}.", symbolToAdjacentParts.entrySet()
      .stream()
      .filter(entry -> entry.getKey().character == '*')
      .filter(entry -> entry.getValue().size() == 2)
      .mapToInt(entry -> entry.getValue().stream().mapToInt(Part::number).reduce(1, (a, b) -> a * b))
      .sum());
  }

  private List<Symbol> parseSymbols(final List<String> input)
  {
    final List<Symbol> symbols = new ArrayList<>();

    for(int row = 0; row < input.size(); row++)
    {
      final String line = input.get(row);
      for(int col = 0; col < line.length(); col++)
      {
        final char character = line.charAt(col);
        if(character != '.' && !Character.isDigit(character))
        {
          final Symbol symbol = new Symbol(character, row, col);
          symbols.add(symbol);
        }
      }
    }

    return symbols;
  }

  private List<Part> parseParts(final List<String> input)
  {
    final List<Part> parts = new ArrayList<>();

    for(int row = 0; row < input.size(); row++)
    {
      final String line = input.get(row);
      for(int col = 0; col < line.length(); col++)
      {
        final char character = line.charAt(col);
        if(Character.isDigit(character))
        {
          final int colStart = col;
          while(col < line.length() && Character.isDigit(line.charAt(col)))
          {
            col++;
          }
          final int colEnd = col;

          final int number = Integer.parseInt(line.substring(colStart, colEnd));
          final Part part = new Part(number, row, colStart, colEnd);
          parts.add(part);
        }
      }
    }

    return parts;
  }

  private Map<Symbol, List<Part>> createSymbolToAdjacentPartsMap(final List<Symbol> symbols, final List<Part> parts)
  {
    final Map<Symbol, List<Part>> map = new HashMap<>();
    for(final Symbol symbol : symbols)
    {
      map.put(symbol, parts.stream().filter(part -> isAdjacent(symbol, part)).toList());
    }
    return map;
  }

  private boolean isAdjacent(final Symbol symbol, final Part part)
  {
    final Point symbolPosition = symbol.getPosition();
    return part.getPositions().stream().anyMatch(partPosition -> PointUtils.isAdjacentInAllDirections(partPosition, symbolPosition));
  }

  private record Symbol(char character, int row, int col)
  {
    public Point getPosition()
    {
      return new Point(col, row);
    }
  }

  private record Part(int number, int row, int colStart, int colEnd)
  {
    public List<Point> getPositions()
    {
      return IntStream.range(colStart, colEnd).mapToObj(col -> new Point(col, row)).toList();
    }
  }
}