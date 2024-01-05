package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Challenge(year = 2023, day = 13)
public class Day13 implements Day
{
  final List<int[][]> parsedInput;

  public Day13()
  {
    final String input = InputUtils.readInputFile(2023, 13);

    this.parsedInput = parseInput(input);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The number is {}.", parsedInput.stream()
      .mapToInt(tuple -> findReflectionRow(tuple[0], 0) * 100 + findReflectionRow(tuple[1], 0))
      .sum());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The number is {}.", parsedInput.stream()
      .mapToInt(tuple -> findReflectionRow(tuple[0], 1) * 100 + findReflectionRow(tuple[1], 1))
      .sum());
  }

  private List<int[][]> parseInput(final String input)
  {
    final List<int[][]> inputAsBinaryLists = new ArrayList<>();
    for(final String pattern : input.split("\\R\\R"))
    {
      final char[][] grid = parseGrid(pattern.split("\\R"));
      final int[][] inputAsBinaryList = parseInput(grid);
      inputAsBinaryLists.add(inputAsBinaryList);
    }
    return inputAsBinaryLists;
  }

  private char[][] parseGrid(final String[] input)
  {
    return Arrays
      .stream(input)
      .map(String::toCharArray)
      .toArray(char[][]::new);
  }

  private int[][] parseInput(final char[][] grid)
  {
    final int[] rotatedCols = new int[grid[0].length];
    final int[] rows = new int[grid.length];
    Arrays.fill(rows, 0);

    for(int col = 0; col < grid[0].length; col++)
    {
      int rotatedBinaryCol = 0;

      for(int row = grid.length - 1; row >= 0; row--)
      {
        int binaryRow = rows[row];

        rotatedBinaryCol = rotatedBinaryCol << 1;
        binaryRow = binaryRow << 1;

        if(grid[row][col] == '#')
        {
          rotatedBinaryCol += 1;
          binaryRow += 1;
        }

        rows[row] = binaryRow;
      }

      rotatedCols[col] = rotatedBinaryCol;
    }

    return new int[][] {rows, rotatedCols};
  }

  private int findReflectionRow(final int[] binaryRows, final int smudge)
  {
    for(int row = 0; row < binaryRows.length - 1; row++)
    {
      final Integer currentRow = binaryRows[row];
      final Integer nextRow = binaryRows[row + 1];

      int mismatches = Integer.bitCount(currentRow ^ nextRow);
      if(mismatches <= smudge)
      {
        final int rowsToCheck = Math.min(row, binaryRows.length - row - 2);
        for(int offset = 1; offset <= rowsToCheck; offset++)
        {
          final int prev = binaryRows[row - offset];
          final int next = binaryRows[row + 1 + offset];

          mismatches += Integer.bitCount(prev ^ next);
          if(mismatches > smudge)
          {
            break;
          }
        }
      }
      if(mismatches == smudge)
      {
        return row + 1;
      }
    }
    return 0;
  }
}