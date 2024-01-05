package de.mpasche.aoc.utils;

import de.mpasche.aoc.common.Direction;
import lombok.experimental.UtilityClass;

import java.awt.*;

/**
 * A utility class for working with Point objects.
 *
 * @author mpasche
 */
@UtilityClass
public class PointUtils
{
  /**
   * Determines whether two points are adjacent to each other.
   *
   * @param firstPoint The coordinates of the first point.
   * @param secondPoint The coordinates of the second point.
   * @return true if the points are adjacent, false otherwise.
   */
  public static boolean isAdjacent(final Point firstPoint, final Point secondPoint)
  {
    final int dx = Math.abs(firstPoint.x - secondPoint.x);
    final int dy = Math.abs(firstPoint.y - secondPoint.y);
    return (dx == 0 && dy <= 1) || (dx <= 1 && dy == 0);
  }

  /**
   * Determines whether two points are adjacent to each other.
   *
   * @param firstPoint The coordinates of the first point.
   * @param secondPoint The coordinates of the second point.
   * @return true if the points are adjacent, false otherwise.
   */
  public static boolean isAdjacentInAllDirections(final Point firstPoint, final Point secondPoint)
  {
    return Math.abs(firstPoint.x - secondPoint.x) <= 1 && Math.abs(firstPoint.y - secondPoint.y) <= 1;
  }

  /**
   * Calculates the Manhattan distance between two points.
   *
   * @param from The starting point.
   * @param to The ending point.
   * @return The distance between the starting point and the ending point.
   */
  public static int getDistance(final Point from, final Point to)
  {
    return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
  }

  /**
   * Determines the direction from one point to another.
   *
   * @param from The starting point.
   * @param to The ending point.
   * @return The direction from the starting point to the ending point.
   */
  public static Direction getDirection(final Point from, final Point to)
  {
    if(from.equals(to))
    {
      return Direction.CENTER;
    }
    else if(from.x == to.x)
    {
      return (from.y < to.y) ? Direction.SOUTH : Direction.NORTH;
    }
    else if(from.y == to.y)
    {
      return (from.x < to.x) ? Direction.EAST : Direction.WEST;
    }
    else if(from.x < to.x)
    {
      return (from.y < to.y) ? Direction.SOUTHEAST : Direction.NORTHEAST;
    }
    else
    {
      return (from.y < to.y) ? Direction.SOUTHWEST : Direction.NORTHWEST;
    }
  }

  /**
   * Moves a given point in a given direction.
   *
   * @param direction The direction to move the point in.
   * @param point The point to be moved.
   * @return The new point after moving in the given direction.
   */
  public static Point move(final Direction direction, final Point point)
  {
    return switch(direction)
    {
      case CENTER -> point.getLocation();
      case NORTH -> new Point(point.x, point.y - 1);
      case NORTHEAST -> new Point(point.x + 1, point.y - 1);
      case EAST -> new Point(point.x + 1, point.y);
      case SOUTHEAST -> new Point(point.x + 1, point.y + 1);
      case SOUTH -> new Point(point.x, point.y + 1);
      case SOUTHWEST -> new Point(point.x - 1, point.y + 1);
      case WEST -> new Point(point.x - 1, point.y);
      case NORTHWEST -> new Point(point.x - 1, point.y - 1);
    };
  }
}
