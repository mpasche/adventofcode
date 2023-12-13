package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Direction;
import de.mpasche.aoc.utils.InputUtils;
import de.mpasche.aoc.utils.PointUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;
import java.util.List;

@Slf4j
@Challenge(year = 2023, day = 10)
public class Day10 implements Day
{
  private final List<Point> path;

  public Day10()
  {
    final String input = InputUtils.readInputFile(2023, 10);

    this.path = parsePath(input);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The farthest point is {} steps away.", path.size() / 2);
  }

  @Override
  public void task2()
  {
    // The Polygon is defined with an even-odd winding rule.
    // This rule determines the "insideness" of a point on the canvas
    // by drawing a ray from that point to infinity in any direction
    // and counting the number of path segments from the given shape that the ray crosses.
    // If this number is odd, the point is inside; if even, the point is outside.
    final Polygon polygon = new Polygon();
    for(final Point point : path)
    {
      polygon.addPoint(point.x, point.y);
    }

    int count = 0;
    final Rectangle bounds = polygon.getBounds();
    for(int x = bounds.x; x < bounds.x + bounds.width; x++)
    {
      for(int y = bounds.y; y < bounds.y + bounds.height; y++)
      {
        final Point point = new Point(x, y);
        if(!path.contains(point) && polygon.contains(point))
        {
          count++;
        }
      }
    }

    log.info("Task 2: {} tiles are enclosed by the loop.", count);
  }

  private List<Point> parsePath(final String input)
  {
    final char[][] grid = Arrays
      .stream(input.split("\\R"))
      .map(String::toCharArray)
      .toArray(char[][]::new);

    final List<Point> path = new ArrayList<>();
    final Deque<Point> deque = new ArrayDeque<>();

    final Point start = getStart(grid);
    deque.addFirst(start);

    while(!deque.isEmpty())
    {
      Point current = deque.removeFirst();
      path.add(current);
      current = findNext(grid, path, current);
      if(current != null)
      {
        deque.addFirst(current);
      }
    }

    return path;
  }

  private Point getStart(final char[][] grid)
  {
    for(int row = 0; row < grid.length; row++)
    {
      for(int col = 0; col < grid[0].length; col++)
      {
        if(grid[row][col] == 'S')
        {
          return new Point(col, row);
        }
      }
    }
    throw new RuntimeException();
  }

  private Point findNext(final char[][] grid, final List<Point> visited, final Point current)
  {
    final char currentTile = grid[current.y][current.x];
    for(final Direction direction : Direction.getCardinalDirections())
    {
      if(hasConnection(currentTile, direction))
      {
        final Point next = PointUtils.move(direction, current);
        final char nextTile = grid[next.y][next.x];
        if(hasConnection(nextTile, direction.getOppositeDirection()) && !visited.contains(next))
        {
          return next;
        }
      }
    }
    return null;
  }

  private boolean hasConnection(final char tile, final Direction direction)
  {
    return switch(tile)
    {
      case '|' -> direction == Direction.NORTH || direction == Direction.SOUTH;
      case '-' -> direction == Direction.EAST || direction == Direction.WEST;
      case 'L' -> direction == Direction.NORTH || direction == Direction.EAST;
      case 'J' -> direction == Direction.NORTH || direction == Direction.WEST;
      case '7' -> direction == Direction.SOUTH || direction == Direction.WEST;
      case 'F' -> direction == Direction.SOUTH || direction == Direction.EAST;
      case 'S' -> true;
      default -> false;
    };
  }
}