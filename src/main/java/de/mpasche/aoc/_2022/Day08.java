package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Challenge(year = 2022, day = 8)
public class Day08 implements Day
{
  private final int[][] grid;

  public Day08()
  {
    this.grid = loadGrid(InputUtils.readInputFileByLine(2022, 8));
  }

  @Override
  public void task1()
  {
    log.info("Task 1: {} trees are visible from outside the grid.", IntStream.range(0, grid.length)
      .flatMap(row -> IntStream.range(0, grid[row].length).filter(col -> isVisible(row, col)))
      .count());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: {} is the highest possible scenic score for any tree.", IntStream.range(0, grid.length)
      .flatMap(row -> IntStream.range(0, grid[row].length).map(col -> countVisible(row, col)))
      .max()
      .orElse(-1));
  }

  private int[][] loadGrid(final List<String> input)
  {
    return input.stream().map(line -> line.chars().map(Character::getNumericValue).toArray()).toArray(int[][]::new);
  }

  private boolean isVisible(final int row, final int col)
  {
    if(row == 0 || row == grid.length - 1 || col == 0 || col == grid[row].length - 1)
    {
      return true;
    }
    final int height = grid[row][col];
    return IntStream.range(0, col).allMatch(i -> grid[row][i] < height)
      || IntStream.range(col + 1, grid[row].length).allMatch(i -> grid[row][i] < height)
      || IntStream.range(0, row).allMatch(i -> grid[i][col] < height)
      || IntStream.range(row + 1, grid.length).allMatch(i -> grid[i][col] < height);
  }

  private int countVisible(final int row, final int col)
  {
    final int height = grid[row][col];
    // left
    int countLeft = 0;
    for(int i = col - 1; i >= 0; i--)
    {
      countLeft++;
      if(grid[row][i] >= height)
      {
        break;
      }
    }
    // right
    int countRight = 0;
    for(int i = col + 1; i < grid[row].length; i++)
    {
      countRight++;
      if(grid[row][i] >= height)
      {
        break;
      }
    }
    // top
    int countTop = 0;
    for(int i = row - 1; i >= 0; i--)
    {
      countTop++;
      if(grid[i][col] >= height)
      {
        break;
      }
    }
    // bottom
    int countBottom = 0;
    for(int i = row + 1; i < grid.length; i++)
    {
      countBottom++;
      if(grid[i][col] >= height)
      {
        break;
      }
    }
    return countLeft * countRight * countTop * countBottom;
  }
}
