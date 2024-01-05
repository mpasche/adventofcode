package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import de.mpasche.aoc.utils.PointUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Challenge(year = 2023, day = 11)
public class Day11 implements Day
{
  private final List<Point> galaxies;
  private final List<Integer> emptyRows;
  private final List<Integer> emptyCols;

  public Day11()
  {
    final String input = InputUtils.readInputFile(2023, 11);

    final char[][] grid = parseGrid(input);
    this.galaxies = parseGalaxies(grid);
    this.emptyRows = parseEmptyRows(grid);
    this.emptyCols = parseEmptyCols(grid);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The sum of these lengths is {}.", getDistances(2));
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The sum of these lengths is {}.", getDistances(1000000));
  }

  private char[][] parseGrid(final String input)
  {
    return Arrays
      .stream(input.split("\\R"))
      .map(String::toCharArray)
      .toArray(char[][]::new);
  }

  private List<Point> parseGalaxies(final char[][] grid)
  {
    final List<Point> galaxies = new ArrayList<>();
    for(int row = 0; row < grid.length; row++)
    {
      for(int col = 0; col < grid[0].length; col++)
      {
        if(grid[row][col] == '#')
        {
          final Point galaxy = new Point(col, row);
          galaxies.add(galaxy);
        }
      }
    }
    return galaxies;
  }

  private List<Integer> parseEmptyRows(final char[][] grid)
  {
    final List<Integer> emptyRows = IntStream
      .range(0, grid.length)
      .boxed()
      .collect(Collectors.toList());

    for(Integer row = 0; row < grid.length; row++)
    {
      for(int col = 0; col < grid[0].length; col++)
      {
        if(grid[row][col] == '#')
        {
          emptyRows.remove(row);
          break;
        }
      }
    }
    return emptyRows;
  }

  private List<Integer> parseEmptyCols(final char[][] grid)
  {
    final List<Integer> emptyCols = IntStream
      .range(0, grid[0].length)
      .boxed()
      .collect(Collectors.toList());

    for(Integer col = 0; col < grid[0].length; col++)
    {
      for(int row = 0; row < grid.length; row++)
      {
        if(grid[row][col] == '#')
        {
          emptyCols.remove(col);
          break;
        }
      }
    }
    return emptyCols;
  }

  private long getDistances(final int expansion)
  {
    long distances = 0;
    for(int i = 0; i < galaxies.size() - 1; i++)
    {
      final Point from = galaxies.get(i);
      for(int j = i + 1; j < galaxies.size(); j++)
      {
        final Point to = galaxies.get(j);
        distances += PointUtils.getDistance(from, to) + getAdditionalDistance(from, to, expansion);
      }
    }
    return distances;
  }

  private long getAdditionalDistance(final Point from, final Point to, final int expansion)
  {
    final int startX = Math.min(from.x, to.x);
    final int endX = Math.max(from.x, to.x);

    final long countX = emptyCols.stream().filter(col -> startX < col && col < endX).count();
    final long distanceX = countX * expansion - countX;

    final int startY = Math.min(from.y, to.y);
    final int endY = Math.max(from.y, to.y);

    final long countY = emptyRows.stream().filter(row -> startY < row && row < endY).count();
    final long distanceY = countY * expansion - countY;

    return distanceX + distanceY;
  }
}