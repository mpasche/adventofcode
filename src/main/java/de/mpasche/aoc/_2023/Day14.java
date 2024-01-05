package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Challenge(year = 2023, day = 14)
public class Day14 implements Day
{
  final char[][] grid;

  public Day14()
  {
    final String input = InputUtils.readInputFile(2023, 14);

    this.grid = parseGrid(input);
  }

  @Override
  public void task1()
  {
    north(grid);
    log.info("Task 1: The total load on the north support beams is {}.", calculateScore(grid));
  }

  @Override
  public void task2()
  {
    move(grid, 1000000000);
    log.info("Task 2: The total load on the north support beams is {}.", calculateScore(grid));
  }

  private char[][] parseGrid(final String input)
  {
    final String[] lines = input.split("\\R");
    return Arrays.stream(lines).map(String::toCharArray).toArray(char[][]::new);
  }

  private void move(final char[][] grid, int times)
  {
    final Map<Integer, Integer> hashToIteration = new HashMap<>();
    boolean checkLoop = true;
    for(int i = 0; i < times; i++)
    {
      if(checkLoop)
      {
        final Integer hash = Arrays.deepHashCode(grid);
        final Integer start = hashToIteration.put(hash, i);
        if(start != null)
        {
          checkLoop = false;
          log.debug("loop between {} and {}", start, i);
          times = i + ((times - i) % (start - i));
        }
      }

      north(grid);
      west(grid);
      south(grid);
      east(grid);
    }
  }

  private void north(final char[][] grid)
  {
    final int rows = grid.length;
    final int cols = grid[0].length;

    for(int col = 0; col < cols; col++)
    {
      for(int row = 1; row < rows; row++)
      {
        if(grid[row][col] == 'O')
        {
          int next = row;
          while(next > 0)
          {
            final char current = grid[next - 1][col];
            if(current == '#' || current == 'O')
            {
              break;
            }
            next--;
          }
          grid[row][col] = '.';
          grid[next][col] = 'O';
        }
      }
    }
  }

  private void south(final char[][] grid)
  {
    final int rows = grid.length;
    final int cols = grid[0].length;

    for(int col = 0; col < cols; col++)
    {
      for(int row = rows - 1; row >= 0; row--)
      {
        if(grid[row][col] == 'O')
        {
          int next = row;
          while(next < rows - 1)
          {
            final char current = grid[next + 1][col];
            if(current == '#' || current == 'O')
            {
              break;
            }
            next++;
          }
          grid[row][col] = '.';
          grid[next][col] = 'O';
        }
      }
    }
  }

  private void west(final char[][] grid)
  {
    final int rows = grid.length;
    final int cols = grid[0].length;

    for(int row = 0; row < rows; row++)
    {
      for(int col = 1; col < cols; col++)
      {
        if(grid[row][col] == 'O')
        {
          int next = col;
          while(next > 0)
          {
            final char current = grid[row][next - 1];
            if(current == '#' || current == 'O')
            {
              break;
            }
            next--;
          }
          grid[row][col] = '.';
          grid[row][next] = 'O';
        }
      }
    }
  }

  private void east(final char[][] grid)
  {
    final int rows = grid.length;
    final int cols = grid[0].length;

    for(int row = 0; row < rows; row++)
    {
      for(int col = cols - 1; col >= 0; col--)
      {
        if(grid[row][col] == 'O')
        {
          int next = col;
          while(next < cols - 1)
          {
            final char current = grid[row][next + 1];
            if(current == '#' || current == 'O')
            {
              break;
            }
            next++;
          }
          grid[row][col] = '.';
          grid[row][next] = 'O';
        }
      }
    }
  }

  private int calculateScore(final char[][] grid)
  {
    int score = 0;
    for(int row = 0; row < grid.length; row++)
    {
      for(final char current : grid[row])
      {
        if(current == 'O')
        {
          score += grid.length - row;
        }
      }
    }
    return score;
  }
}